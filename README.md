android
=======


- `git clone https://github.com/Shace/android.git`
- cp android/app/src/main/assets/shace.example.properties android/app/src/main/assets/shace.properties
- Set the correct values in android/app/src/main/assets/shace.properties
- [Install Android Studio](https://developer.android.com/sdk/installing/studio.html) (The one with the Android SDK)
- File -> Open -> Select the folder android created by git
- Open the SDK manager (The icon with the Droid and a blue box with an arrow)
    - In "Tools", install *SDK Tools*, *SDK Platform-tools* ans the **latest** *SDK Build-tools*
    - Install the *SDK Platform*, *ARM Image*, *Intel image*, and the *Google APIs* of the **latest stable** version of Android (**Warning** 4.4W means Android Wear) **as well as** the latest **preview** (if there's any)
    - In "Extra", install **all** the *Android Support*
- [Install HAXM](https://www.udacity.com/course/viewer#!/c-ud853/l-1395568821/m-2001148615)
- If you have an Android phone, connect it by USB
- If you don't have an Android phone, open the AVD manager (the purple square) and add a new virtual phone
- Build -> Make project
- Run -> Run 'app' (Or click on the green arrow)

If you don't `Build -> Make project` before Running it, you will have some error about unexisting file (creating by Android Annotations)


## Resources

* Doc [Android Developer](https://developer.android.com/index.html)
* Doc [Android API Reference](https://developer.android.com/reference/packages.html)
* Doc: [Material Designs](http://www.google.com/design/spec/material-design/introduction.html)
* Course by Google: [Developing Android Apps](https://www.udacity.com/course/viewer#!/c-ud853/l-1395568821/m-1643858568)
* Course by Google: [UX Design for Mobile Developers](https://www.udacity.com/course/viewer#!/c-ud849/l-1613818915/e-1581848542/m-1581848543)
