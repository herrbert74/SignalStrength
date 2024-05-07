# SignalStrength

An app to retrieve and display signal strength from the Ofcom API.

# Setup

Add an API key from https://api.ofcom.org.uk/ to your global gradle.properties or local.properties under the name 
"ofcomAccessToken".

# Remarks from Zsolt Bertalan

## Tech

* I used commonly used libraries Coroutines, Dagger 2, AndroidX, and Compose.
* I also used less commonly used libraries, like MVIKotlin, Decompose and Essenty, mainly because I set up templates 
  with them. See details in the Structure and Libraries sections. I can write code with MVVM and other Google 
  recommended practices as well.

## Structure

* I use a monorepo for such a tiny project, however I used a few techniques to show how I can build an app that 
  scales, even if they are an overkill as they are now.
* The three main sections (module groups in a larger project) are **data**, **domain**, and **ui**.
* **Domain** does not depend on anything and contains the api interfaces, and the model classes.
* **Data** implements the domain interfaces (repos) through the network, local and db packages or modules, and does not 
  depend on anything else, apart from platform and third party libraries.
* **Ui** uses the data implementations through dependency injection and the domain entities. **Root** package 
  provides the root implementations for Decompose Business Logic Components (BLoCs) and navigation.
* **Di** Dependency Injection through Dagger and Hilt

## Libraries used

* **MVIKotlin**, **Decompose** and **Essenty** are libraries from the same developer, who I know personally. Links to 
  the libraries:
  * https://github.com/arkivanov/MVIKotlin
    * An MVI library used on the screen or component level.
  * https://github.com/arkivanov/Decompose
    * A component based library built for Compose with Kotlin Multiplatform in mind, and provides the glue, what 
      normally the ViewModel and Navigation library does, but better. 
  * https://github.com/arkivanov/Essenty
    * Has some lifecycle and ViewModel wrappers and replacements.

## Justification for above libraries

* I do not think that starting an app with a scalable architecture is over engineering, but some people do think so. 
  I respectfully disagree.
* I've used MVIKotlin professionally for years.
* I adopted MVIKotlin, then Decompose for private projects too, so I'm most proficient with these. I could have used 
  the standard MVVM and Jetpack Navigation, but it would take more time for me at this point.
* These libraries address issues, that the Google libraries failed to address. So no ViewModels, for example.
* Risks commonly associated with above libraries
  * Abandoned by author: No real risk, as I can copy to my project, others can pick up, or I can switch to similar.
  * Onboarding: For most people it will be new. That's a real problem, but the libraries are getting popular.
  * More boilerplate initially: they need some initial setup, but they scale well for more screens.

## Problems dealt with

* UI according to the requirements.
* Separate DTO and Domain classes, and extensive mapping between them to facilitate effective presentation.
* Check button is only enabled, if the Postcode string is satisfying the postcode regex.
* User can drill down to house number and Mobile provider to get to the dashboard with all the data that the API 
  provides.
* Error handling.
  * Accepting postcodes with lower case letters and spaces, which the API fails to process.
  * PostcodeException for invalid postcodes shown on the TextField.
  * Other errors including unexpected errors are shown as a SnackBar.
* Repository is main safe.
* Postcode input is debounced, so the app is not pounding on resources in case of a monkey type usage.
* API secret is hidden, see setup requirement.

## Room for improvement

* Write Unit tests and UI tests.
* Some Compose elements might be made more effective.
* I did not have the time for a nice launcher icon.
