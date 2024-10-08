package co.tz.settlo.api.common.services;


import co.tz.settlo.api.controllers.country.CountryDTO;
import co.tz.settlo.api.controllers.country.CountryService;
import co.tz.settlo.api.controllers.expense_category.ExpenseCategory;
import co.tz.settlo.api.controllers.expense_category.ExpenseCategoryDTO;
import co.tz.settlo.api.controllers.expense_category.ExpenseCategoryService;
import co.tz.settlo.api.controllers.role.RoleService;
import co.tz.settlo.api.controllers.subscription.SubscriptionDTO;
import co.tz.settlo.api.controllers.subscription.SubscriptionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleService roleService;
    private final CountryService countryService;
    private final SubscriptionService subscriptionService;
    private final ExpenseCategoryService expenseCategoryService;

    public DataSeeder(RoleService roleService, CountryService countryService, SubscriptionService subscriptionService, ExpenseCategoryService expenseCategoryService) {
        this.roleService = roleService;
        this.countryService = countryService;
        this.subscriptionService = subscriptionService;
        this.expenseCategoryService = expenseCategoryService;
    }

    @Override
    public void run(String... args) {
        // Seed user roles
//        createRoleIfNotExists("User");
//        createRoleIfNotExists("Admin");
//        createRoleIfNotExists("Owner");

        // Seed currencies & countries
        createCountryIfNotExists("USD", "US Dollar", "US", true, "US", "United States", "en_US", true);
        createCountryIfNotExists("EUR", "Euro", "EU", true, "DE", "Germany", "de_DE", true);
        createCountryIfNotExists("JPY", "Japanese Yen", "JP", false, "JP", "Japan", "ja_JP", false);
        createCountryIfNotExists("GBP", "British Pound", "GB", true, "GB", "United Kingdom", "en_GB", true);
        createCountryIfNotExists("CNY", "Chinese Yuan", "CN", false, "CN", "China", "zh_CN", false);
        createCountryIfNotExists("AUD", "Australian Dollar", "AU", false, "AU", "Australia", "en_AU", false);
        createCountryIfNotExists("CAD", "Canadian Dollar", "CA", false, "CA", "Canada", "en_CA", false);
        createCountryIfNotExists("CHF", "Swiss Franc", "CH", false, "CH", "Switzerland", "de_CH", false);
        createCountryIfNotExists("HKD", "Hong Kong Dollar", "HK", false, "HK", "Hong Kong", "zh_HK", false);
        createCountryIfNotExists("SGD", "Singapore Dollar", "SG", false, "SG", "Singapore", "en_SG", false);
        createCountryIfNotExists("SEK", "Swedish Krona", "SE", false, "SE", "Sweden", "sv_SE", false);
        createCountryIfNotExists("KRW", "South Korean Won", "KR", false, "KR", "South Korea", "ko_KR", false);
        createCountryIfNotExists("NOK", "Norwegian Krone", "NO", false, "NO", "Norway", "nb_NO", false);
        createCountryIfNotExists("NZD", "New Zealand Dollar", "NZ", false, "NZ", "New Zealand", "en_NZ", false);
        createCountryIfNotExists("INR", "Indian Rupee", "IN", false, "IN", "India", "hi_IN", false);
        createCountryIfNotExists("MXN", "Mexican Peso", "MX", false, "MX", "Mexico", "es_MX", false);
        createCountryIfNotExists("TWD", "New Taiwan Dollar", "TW", false, "TW", "Taiwan", "zh_TW", false);
        createCountryIfNotExists("ZAR", "South African Rand", "ZA", false, "ZA", "South Africa", "en_ZA", false);
        createCountryIfNotExists("BRL", "Brazilian Real", "BR", false, "BR", "Brazil", "pt_BR", false);
        createCountryIfNotExists("DKK", "Danish Krone", "DK", false, "DK", "Denmark", "da_DK", false);
        createCountryIfNotExists("PLN", "Polish Złoty", "PL", false, "PL", "Poland", "pl_PL", false);
        createCountryIfNotExists("THB", "Thai Baht", "TH", false, "TH", "Thailand", "th_TH", false);
        createCountryIfNotExists("IDR", "Indonesian Rupiah", "ID", false, "ID", "Indonesia", "id_ID", false);
        createCountryIfNotExists("HUF", "Hungarian Forint", "HU", false, "HU", "Hungary", "hu_HU", false);
        createCountryIfNotExists("CZK", "Czech Koruna", "CZ", false, "CZ", "Czech Republic", "cs_CZ", false);
        createCountryIfNotExists("ILS", "Israeli New Shekel", "IL", false, "IL", "Israel", "he_IL", false);
        createCountryIfNotExists("CLP", "Chilean Peso", "CL", false, "CL", "Chile", "es_CL", false);
        createCountryIfNotExists("PHP", "Philippine Peso", "PH", false, "PH", "Philippines", "en_PH", false);
        createCountryIfNotExists("AED", "United Arab Emirates Dirham", "AE", true, "AE", "United Arab Emirates", "ar_AE", true);
        createCountryIfNotExists("COP", "Colombian Peso", "CO", false, "CO", "Colombia", "es_CO", false);
        createCountryIfNotExists("SAR", "Saudi Riyal", "SA", false, "SA", "Saudi Arabia", "ar_SA", false);
        createCountryIfNotExists("MYR", "Malaysian Ringgit", "MY", false, "MY", "Malaysia", "ms_MY", false);
        createCountryIfNotExists("RON", "Romanian Leu", "RO", false, "RO", "Romania", "ro_RO", false);
        createCountryIfNotExists("TRY", "Turkish Lira", "TR", false, "TR", "Turkey", "tr_TR", false);
        createCountryIfNotExists("RUB", "Russian Ruble", "RU", false, "RU", "Russia", "ru_RU", false);
        createCountryIfNotExists("NGN", "Nigerian Naira", "NG", true, "NG", "Nigeria", "en_NG", true);
        createCountryIfNotExists("PKR", "Pakistani Rupee", "PK", false, "PK", "Pakistan", "ur_PK", false);
        createCountryIfNotExists("EGP", "Egyptian Pound", "EG", false, "EG", "Egypt", "ar_EG", false);
        createCountryIfNotExists("VND", "Vietnamese Đồng", "VN", false, "VN", "Vietnam", "vi_VN", false);
        createCountryIfNotExists("BGN", "Bulgarian Lev", "BG", false, "BG", "Bulgaria", "bg_BG", false);
        createCountryIfNotExists("KES", "Kenyan Shilling", "KE", true, "KE", "Kenya", "en_KE", true);
        createCountryIfNotExists("UAH", "Ukrainian Hryvnia", "UA", false, "UA", "Ukraine", "uk_UA", false);
        createCountryIfNotExists("MAD", "Moroccan Dirham", "MA", false, "MA", "Morocco", "ar_MA", false);
        createCountryIfNotExists("QAR", "Qatari Riyal", "QA", false, "QA", "Qatar", "ar_QA", false);
        createCountryIfNotExists("KWD", "Kuwaiti Dinar", "KW", false, "KW", "Kuwait", "ar_KW", false);
        createCountryIfNotExists("DZD", "Algerian Dinar", "DZ", false, "DZ", "Algeria", "ar_DZ", false);
        createCountryIfNotExists("ARS", "Argentine Peso", "AR", false, "AR", "Argentina", "es_AR", false);
        createCountryIfNotExists("UYU", "Uruguayan Peso", "UY", false, "UY", "Uruguay", "es_UY", false);
        createCountryIfNotExists("PEN", "Peruvian Sol", "PE", false, "PE", "Peru", "es_PE", false);
        createCountryIfNotExists("TZS", "Tanzanian Shilling", "TZ", true, "TZ", "Tanzania", "sw_TZ", true);

        // Seed subscriptions
        createSubscriptionIfNotExists(0.0, 0.0,"Trial", "trl", true);

        // seed expense categories
        // source https://medium.com/@BuySimply/10-common-expense-categories-every-business-owner-should-be-tracking-84c9738baea5
        createExpenseCategoryIfNotExists("Employee Salary and Benefits");
        createExpenseCategoryIfNotExists("Office Equipment/Supplies");
        createExpenseCategoryIfNotExists("Rent and Utilities");
        createExpenseCategoryIfNotExists("Marketing Expenses");
        createExpenseCategoryIfNotExists("Business Dues and Subscriptions");
        createExpenseCategoryIfNotExists("Maintenance and Repairs");
        createExpenseCategoryIfNotExists("Legal and Professional Fees");
        createExpenseCategoryIfNotExists("Transport Expenses");
        createExpenseCategoryIfNotExists("Vehicle Expenses");
        createExpenseCategoryIfNotExists("Charitable Contributions");
    }

//    private void createRoleIfNotExists(String roleName) {
//        final boolean doesRoleExist = roleService.nameExists(roleName);
//
//        if (!doesRoleExist) {
//            RoleDTO role = new RoleDTO();
//            role.setName(roleName);
//            role.setStatus(true);
//            role.setIsArchived(false);
//            role.set
//            role.setStatus(true);
//            roleService.create(role);
//        }
//    }

    @Transactional
    protected void createCountryIfNotExists(
            String currencyCode, String currencyName, String currencyLocale, Boolean currencySupport,
            String countryCode, String countryName, String countryLocale, Boolean countrySupport) {

        final boolean doesCountryExist = countryService.codeExists(countryCode);

        if (!doesCountryExist) {
            CountryDTO country = new CountryDTO();

            country.setCode(countryCode);
            country.setName(countryName);
            country.setLocale(countryLocale);
            country.setCurrencyCode(currencyCode);
            country.setSupported(countrySupport);
            country.setStatus(true);
            country.setIsArchived(false);
            country.setCanDelete(true);

            countryService.create(country);
        }
    }

    @Transactional
    protected void createSubscriptionIfNotExists(
            Double amount, Double discount, String packageName, String packageCode, Boolean isTrial
    ) {
        final boolean subscriptionExists = subscriptionService.packageCodeExists(packageCode);

        if (!subscriptionExists) {
            SubscriptionDTO subscription = new SubscriptionDTO();

            subscription.setAmount(amount);
            subscription.setDiscount(discount);
            subscription.setPackageCode(packageCode);
            subscription.setPackageName(packageName);
            subscription.setIsTrial(isTrial);
            subscription.setStatus(true);
            subscription.setCanDelete(true);
            subscription.setIsArchived(false);


            subscriptionService.create(subscription);
        }
    }

    @Transactional
    protected void createExpenseCategoryIfNotExists(final String expenseName ) {
        final boolean expenseCategoryExists = expenseCategoryService.nameExists(expenseName);

        if (!expenseCategoryExists) {
            ExpenseCategoryDTO expenseCategoryDTO = new ExpenseCategoryDTO();

            expenseCategoryDTO.setName(expenseName);
            expenseCategoryDTO.setStatus(true);
            expenseCategoryDTO.setCanDelete(true);
            expenseCategoryDTO.setIsArchived(false);


            expenseCategoryService.create(expenseCategoryDTO);
        }
    }
}
