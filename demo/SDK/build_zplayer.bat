@echo ""
@echo "Compiling Java..."

@del /F /S /Q  com
@rd com /S /Q

@javac ZLPlayerActivity.java -source 1.6  -target 1.6  -bootclasspath classes.jar   -classpath  android.jar;common.jar;SDK.jar  -d .
@javap -s com.zulong.test.ZLPlayerActivity
@jar cvfM ZLPlayerActivity.jar com/

@del /F /S /Q  com
@rd com /S /Q

@echo ""
@echo "Done!"
@pause