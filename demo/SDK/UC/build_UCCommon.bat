@echo ""
@echo "Compiling Java..."

@del /F /S /Q  com
@rd com /S /Q
@javac -encoding UTF-8 UCConfigReader.java  UCSDK.java UCLoginParams.java  UCLoginTask.java  -source 1.6  -target 1.6  -bootclasspath ..\android.jar;..\..\SDKLibs\UC\alipay-20150513.jar;..\..\SDKLibs\UC\UCGameSDK-3.5.3.1.jar -classpath  ..\SDK.jar -d .



@jar cvfM common.jar com/

@copy .\common.jar ..\common.jar

@del /F /S /Q  com
@rd com /S /Q


@echo ""
@echo "Done!"
@pause