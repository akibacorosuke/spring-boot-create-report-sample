package root.constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateReportSampleConstants {

    public static final String WORK_PATH = String.valueOf(System.getProperty("java.io.tmpdir")) + "/work/";

    public static final String EXPORT_PATH = String.valueOf(System.getProperty("java.io.tmpdir")) + "/export/";

    public static final String FILE_NAME = "inventory_info_" + (new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")).format(new Date()) + ".csv";

    public static final String FILDER_ID = "";

    /** messages **/
    public static final String MSG_INFO_EXEC_START = "info.exec.start";

    public static final String MSG_INFO_EXEC_END = "info.exec.end";

    public static final String MSG_INFO_ITEM_INFO = "info.item.info";

    public static final String MSG_INFO_ITEM_STATUS = "info.item.status";

    public static final String MSG_INFO_FILE_CREATE = "info.file.create";

    public static final String MSG_INFO_FILE_UPLOAD = "info.file.upload";

    public static final String MSG_ERROR_EXEC_END = "error.exec.end";

    public static final String MSG_ERROR_NO_ITEM_INFO = "error.no.item.info";
}
