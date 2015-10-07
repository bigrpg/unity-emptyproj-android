@echo ""
@echo "Compiling Java..."

@del /F /S /Q  com
@rd com /S /Q

@javac  CInterface.java  LoginRet.java PayRet.java PayParams.java  -source 1.6  -target 1.6  -bootclasspath ../classes.jar   -classpath  ../android.jar;../common.jar;../SDK.jar  -d .

@jar cvfM CInterface.jar com/


@del /F /S /Q  com
@rd com /S /Q

@echo ""
@echo "Done!"
@pause