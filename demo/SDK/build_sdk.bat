@echo ""
@echo "Compiling Java..."

@del /F /S /Q  com
@rd com /S /Q

@javac  LogUtil.java Param.java  ConfigReader.java  ConfigFileException.java  SDKInterface.java SDKFoundation.java  SDKImpl.java  SDKBase.java FloatViewItem.java  Toast.java  BaseParams.java  BaseLoginParams.java  Account.java  LoginTask.java PayParameterLackException.java  BaseOrderParams.java OrderParams.java  PayTask.java  -source 1.6  -target 1.6  -bootclasspath classes.jar   -classpath  android.jar  -d .

@jar cvfM SDK.jar com/


@del /F /S /Q  com
@rd com /S /Q

@echo ""
@echo "Done!"
@pause