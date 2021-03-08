package com.alexsanderfranco.gistapp.list

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexsanderfranco.gistapp.R
import com.alexsanderfranco.gistapp.list.repository.GistListRepository
import com.alexsanderfranco.gistapp.list.view.GistListActivity
import com.alexsanderfranco.gistapp.shared.factory.GistFactory
import com.alexsanderfranco.gistapp.shared.test.SimpleIdlingResource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject


@RunWith(AndroidJUnit4::class)
class GistListActivityTest {

    private lateinit var context: Context
    private lateinit var activityScenario: ActivityScenario<GistListActivity>
    private val idlingResource: SimpleIdlingResource by inject(SimpleIdlingResource::class.java)

    @MockK
    private lateinit var mockRepository: GistListRepository
    private val listOfGists = GistFactory().makeListOfGist()
    private val gist = listOfGists.first()

    @Before
    fun setUp() {
        setUpMocks()
        context = ApplicationProvider.getApplicationContext()
        activityScenario = ActivityScenario.launch(GistListActivity::class.java)
        activityScenario.onActivity { IdlingRegistry.getInstance().register(idlingResource) }
    }

    private fun setUpMocks() {
        MockKAnnotations.init(this)
        val mockModule = module {
            single(override = true) { mockRepository }
        }
        loadKoinModules(mockModule)

        coEvery { mockRepository.fetchGistList(any()) } returns listOfGists
        coEvery { mockRepository.fetchFavorites() } returns
                (listOfGists.filter { item -> item.isFavorite })
        every { mockRepository.toggleFavorite(gist) } answers { gist.isFavorite = !gist.isFavorite }
    }

    @Test
    fun whenActivityIsLaunchedShouldShowContent() {
        onView(withId(R.id.gists_rv)).check(matches(isDisplayed()))
        onView(withId(R.id.gists_rv)).apply {
            check(matches(isDisplayed()))
            hasDescendant(withTagValue(`is`(gist.id)))
        }
    }

    @Test
    fun whenFabClickedShouldShowSearchBar() {
        onView(withId(R.id.search_fab)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.search_et)).check(matches(isDisplayed()))
    }

    @Test
    fun whenSearchedShouldShowResults() {
        val name = gist.owner?.login
        onView(withId(R.id.search_fab)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.search_et)).check(matches(isDisplayed()))
            .perform(typeText(name))
        val searchButtonText = context.resources.getString(R.string.search_action)
        onView(withText(searchButtonText)).inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.gists_rv)).apply {
            check(matches(isDisplayed()))
            hasDescendant(withText(name))
        }
    }

    @Test
    fun whenFavoritesSelectedShouldShowFavorites() {
        onView(withId(R.id.show_favorites_sw)).check(matches(isDisplayed()))
        onView(withId(R.id.show_favorites_sw)).perform(click())
        val firstFavorite = listOfGists.first { gist -> gist.isFavorite }
        onView(withId(R.id.gists_rv)).apply {
            check(matches(isDisplayed()))
            hasDescendant(withTagValue(`is`(firstFavorite.id)))
        }
    }

    @Test
    fun whenFavoriteClickedShouldUpdateIcon() {
        gist.apply {
            onView(withId(R.id.gists_rv)).apply {
                check(matches(isDisplayed()))
                hasDescendant(withTagValue(`is`(id)))
            }

            onView(withTagValue(`is`(id))).apply {
                when (isFavorite) {
                    true -> hasDescendant(withTagValue(`is`(R.drawable.ic_favorite)))
                    false -> hasDescendant(withTagValue(`is`(R.drawable.ic_favorite_border)))
                }
                hasDescendant(withId(R.id.gist_item_favorite_ib))
            }
            onView(
                allOf(withId(R.id.gist_item_favorite_ib), isDescendantOfA(withTagValue(`is`(id))))
            ).apply {
                perform(click())
                when (isFavorite) {
                    true -> hasDescendant(withTagValue(`is`(R.drawable.ic_favorite)))
                    false -> hasDescendant(withTagValue(`is`(R.drawable.ic_favorite_border)))
                }
            }
            onView(withId(R.id.show_favorites_sw)).perform(click())
            onView(withId(R.id.gists_rv)).apply {
                check(matches(isDisplayed()))
                hasDescendant(withTagValue(`is`(id)))
            }
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}