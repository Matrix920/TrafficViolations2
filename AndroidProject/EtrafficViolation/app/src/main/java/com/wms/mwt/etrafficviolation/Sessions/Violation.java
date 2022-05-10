package  com.wms.mwt.etrafficviolation.Sessions;

/**
 * Created by Matrix on 12/19/2017.
 */

public class Violation {
    public static final String VIOLATION_ID="ViolationID";
    public static final String VIOLATION_TYPE="ViolationType";
    public static final String TAX="Tax";
    public static final String VIOLATIONS="violations";

    public int violationID;
    public String violationType;
    public double tax;

    public Violation(int violationID, String violationType, double tax) {
        this.violationID = violationID;
        this.violationType = violationType;
        this.tax = tax;
    }
}
