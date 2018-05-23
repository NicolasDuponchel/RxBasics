package duponchel.nicolas.rxbasics

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import duponchel.nicolas.rxbasics.MainViewModel.InstallationStatus.SUCCESS
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.LOADING
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.NOT_LOADING
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnit

class MainViewModelTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @InjectMocks
    private lateinit var classUnderTest: MainViewModel

    @Test
    fun `init set joke list to empty`() {
        val jokes = classUnderTest.jokes.testObserver()

        Truth.assert_()
            .that(jokes.observedValues.first()).isEmpty()
    }

    @Test
    fun `onJokeRequest set correct loading states`() {
        val jokeStatus = classUnderTest.jokeLoadingStatus.testObserver()

        classUnderTest.onJokeRequest()

        Truth.assert_()
            .that(jokeStatus.observedValues)
            .isEqualTo(listOf(LOADING, NOT_LOADING))
    }

    @Test
    fun `onFakeInstall set correct live data values`() {
        val installationStatus = classUnderTest.installation.testObserver()
        val installationLoadingStatus = classUnderTest.installLoadingStatus.testObserver()

        classUnderTest.onFakeInstall()
        //Note that this tests the real call to onFakeInstall(). It will take 3 seconds. It's possible to play with time in RX unit testing. See a next article maybe :P

        Truth.assert_()
            .that(installationStatus.observedValues.first())
            .isEqualTo(SUCCESS)

        Truth.assert_()
            .that(installationLoadingStatus.observedValues)
            .isEqualTo(listOf(LOADING, NOT_LOADING))
    }
}