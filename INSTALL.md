INSTALL ANDROID STUDIO
1. Download the .exe file here: https://developer.android.com/studio
2. Run the downloaded file and install it. (internet connection needed)
3. For the setup, choose STANDARD install type
4. After verifying the settings, confirm the download

ADD OPENCV ANDROID IN ANDROID STUDIO
1. Download and install Java (https://www.oracle.com/java/technologies/javase-downloads.html)
2. Download the OpenCV Library, then extract the file (https://opencv.org/android/)
3. Open Android Studio and create new project (or open existing project)
4. In Android Studio > New > Import Module 
5. Import the "java" folder from the extracted file (OpenCV Anroid SDK; from #2)
	opencv/opencv/sdk/java
	A. If there is an error, go to Project view (tool window bar on the left-most, click "Project") > opencv folder and open the build.gradle (opencv)
	C. Go to Project view > app and open the build.gradle (app)
	D. Make sure that the compliedSdkVersion, buildToolsVersion, and targetSdkVersion on the build.gradle of "app" is the same versions that the build.gradle of "opencvlibrary" has
	E. Sync 
6. Add the module dependency	
	A. Go to File > Project Structure > app 
	B. Select the "Dependencies"
	C. Click the "+" on the right side and select "Module Dependency" 
	D. Select OpenCV module
	E. Click "OK"
7. Go to extracted file (from #2)
	opencv > opencv > sdk > native > libs
	A. Copy the folliwing folders:
	 	arm64-v8a
	 	armeabi-v7a
	 	x86
	 	x86_64

	 	*name of files might vary according to the version downloaded

	B. Go to Android Studio, then Project view > app, then right-click then select New > Folder > JNI Folder then paste the 4 folders inside