package mm.com.mytel.training_project.library_management_system.common.util;

public class StringUtils {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
