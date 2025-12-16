package flows;

import pages.BeneficiaryPage;

import java.util.Map;

public class BeneficiaryFlow {

    private final BeneficiaryPage page = new BeneficiaryPage();

    public void addBeneficiary(Map<String, String> data, String type) {
        switch (type.toUpperCase()) {
            case "SWIFT": page.addSwift(data); break;
            case "OLB":
            case "LOCAL": page.addOlb(data); break;
            case "OST":
            case "SBM":  page.addOst(data); break;
        }
    }

    public void editBeneficiary(Map<String, String> data, String type) {
        switch (type.toUpperCase()) {
            case "SWIFT": page.editSwift(data); break;
            case "OLB":
            case "LOCAL": page.editOlb(data); break;
            case "OST":
            case "SBM":  page.editOst(data); break;
        }
    }

    public void payBeneficiary(Map<String, String> data, String type) {
        switch (type.toUpperCase()) {
            case "SWIFT": page.paySwift(data); break;
            case "OLB":
            case "LOCAL": page.payOlb(data); break;
            case "OST":
            case "SBM":  page.payOst(data); break;
        }
    }

    public void deleteBeneficiary(Map<String, String> data, String type) {
        switch (type.toUpperCase()) {
            case "SWIFT": page.deleteSwift(data); break;
            case "OLB":
            case "LOCAL": page.deleteOlb(data); break;
            case "OST":
            case "SBM":  page.deleteOst(data); break;
        }
    }
}
