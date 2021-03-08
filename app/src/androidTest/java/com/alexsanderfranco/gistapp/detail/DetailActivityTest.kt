package com.alexsanderfranco.gistapp.detail

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexsanderfranco.gistapp.R
import com.alexsanderfranco.gistapp.database.entity.Gist
import com.alexsanderfranco.gistapp.detail.repository.DetailRepository
import com.alexsanderfranco.gistapp.detail.view.DetailActivity
import com.alexsanderfranco.gistapp.shared.factory.GistFactory
import com.alexsanderfranco.gistapp.shared.test.SimpleIdlingResource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    private lateinit var context: Context
    private lateinit var activityScenario: ActivityScenario<DetailActivity>
    private val idlingResource: SimpleIdlingResource by inject(SimpleIdlingResource::class.java)

    @MockK
    private lateinit var mockRepository: DetailRepository
    private val gist: Gist = GistFactory().makeGist()
    private val intent: Intent by lazy {
        Intent(context, DetailActivity::class.java).also { intent ->
            intent.putExtra(DetailActivity.GIST_PARCELABLE, gist)
        }
    }

    @Before
    fun setUp() {
        setUpMocks()
        context = ApplicationProvider.getApplicationContext()
        activityScenario = ActivityScenario.launch(intent)
        activityScenario.onActivity { IdlingRegistry.getInstance().register(idlingResource) }
    }

    private fun setUpMocks() {
        MockKAnnotations.init(this)
        val mockModule = module {
            single(override = true) { mockRepository }
        }
        loadKoinModules(mockModule)

        every { mockRepository.toggleFavorite(gist) } answers { gist.isFavorite = !gist.isFavorite }
    }

    @Test
    fun whenActivityIsLaunchedShouldShowContent() {
        onView(withId(R.id.gist_owner_name_tv))
            .check(matches(withText(gist.owner?.login)))
        onView(withId(R.id.gist_date_tv))
            .check(matches(withText(gist.createdAt)))
        onView(withId(R.id.gist_favorite_ib)).check(matches(isDisplayed()))
        when (gist.isFavorite) {
            true -> onView(withTagValue(`is`(R.drawable.ic_favorite)))
                .check(matches(isDisplayed()))
            false -> onView(withTagValue(`is`(R.drawable.ic_favorite_border)))
                .check(matches(isDisplayed()))
        }
        onView(withId(R.id.gist_file_list_rv)).check(matches(isDisplayed()))

        val file = gist.files?.first()
        file?.apply {
            onView(withTagValue(`is`(filename))).apply {
                check(matches(isDisplayed()))
                hasDescendant(withId(R.id.gist_file_item_filename_tv))
                hasDescendant(withId(R.id.gist_file_item_type_tv))
                hasDescendant(withText(filename))
                hasDescendant(withText(type))
            }
        }
    }

    @Test
    fun whenFavoriteClickedShouldUpdateIcon() {
        onView(withId(R.id.gist_favorite_ib)).check(matches(isDisplayed())).perform(click())
        when (gist.isFavorite) {
            true -> onView(withTagValue(`is`(R.drawable.ic_favorite)))
                .check(matches(isDisplayed()))
            false -> onView(withTagValue(`is`(R.drawable.ic_favorite_border)))
                .check(matches(isDisplayed()))
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}