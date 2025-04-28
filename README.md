# MusicPlayer Test for Util

This is a test for the util class for the musicplayer project. A seperate codebase was made since some source code was modified with hard path to make code work.
The test is a simple test to check if the util class works as expected.

### Running code
After cloning the repo, with jdk 1.8 full, (normal jdk doesn't include javafx) run the following command in the root directory of the project:
```
./gradlew test --tests "util.*" jacocoTestReport
```

This should create a test report in the `build/reports/tests/test/index.html` file.

Screenshot of the test report was also added to coco_report for your convenience.
### Notes

There are tests that check multiple threads. There are tests with set time to make sure that threads collide. The time would need some modification in different hardware.
ControlPanelTableCell was not tested as it should be tested in the view project.
As XMLEditorTest is a test for static methods, when the tests are all run, the method creates an error. 
In order to avoid errors, test runs should be run separately.
For the Jacoco report, all tests were run together.

Please check the slideshow for more information about the test.
