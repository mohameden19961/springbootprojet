package supnum.projet.Library.controllers;

import supnum.projet.Library.data.entities.Language;
import supnum.projet.Library.data.entities.Nationality;
import supnum.projet.Library.data.repositories.LanguageRepository;
import supnum.projet.Library.data.repositories.NationalityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/seed")
public class SeedController {

    private final LanguageRepository languageRepository;
    private final NationalityRepository nationalityRepository;

    public SeedController(LanguageRepository languageRepository, NationalityRepository nationalityRepository) {
        this.languageRepository = languageRepository;
        this.nationalityRepository = nationalityRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Integer>> seed() {
        int langCount = languageRepository.saveAll(languages()).size();
        int natCount = nationalityRepository.saveAll(nationalities()).size();

        return ResponseEntity.ok(Map.of(
            "languages", langCount,
            "nationalities", natCount
        ));
    }

    private List<Language> languages() {
        return List.of(
            lang("AR", "Arabic"), lang("BN", "Bengali"),
            lang("DE", "German"), lang("EN", "English"),
            lang("ES", "Spanish"), lang("FR", "French"),
            lang("HI", "Hindi"), lang("IT", "Italian"),
            lang("JA", "Japanese"), lang("KO", "Korean"),
            lang("NL", "Dutch"), lang("PL", "Polish"),
            lang("PT", "Portuguese"), lang("RU", "Russian"),
            lang("SV", "Swedish"), lang("TR", "Turkish"),
            lang("UK", "Ukrainian"), lang("ZH", "Chinese")
        );
    }

    private List<Nationality> nationalities() {
        return List.of(
            nat("AD", "Andorran"), nat("AE", "Emirati"),
            nat("AL", "Albanian"), nat("AR", "Argentinian"),
            nat("AT", "Austrian"), nat("AU", "Australian"),
            nat("BD", "Bangladeshi"), nat("BE", "Belgian"),
            nat("BG", "Bulgarian"), nat("BR", "Brazilian"),
            nat("CA", "Canadian"), nat("CH", "Swiss"),
            nat("CI", "Ivorian"), nat("CL", "Chilean"),
            nat("CN", "Chinese"), nat("CO", "Colombian"),
            nat("CU", "Cuban"), nat("CY", "Cypriot"),
            nat("CZ", "Czech"), nat("DE", "German"),
            nat("DK", "Danish"), nat("DZ", "Algerian"),
            nat("EG", "Egyptian"), nat("ES", "Spanish"),
            nat("ET", "Ethiopian"), nat("FI", "Finnish"),
            nat("FR", "French"), nat("GB", "British"),
            nat("GH", "Ghanaian"), nat("GR", "Greek"),
            nat("HK", "Hongkonger"), nat("HR", "Croatian"),
            nat("HU", "Hungarian"), nat("ID", "Indonesian"),
            nat("IE", "Irish"), nat("IL", "Israeli"),
            nat("IN", "Indian"), nat("IQ", "Iraqi"),
            nat("IR", "Iranian"), nat("IS", "Icelander"),
            nat("IT", "Italian"), nat("JM", "Jamaican"),
            nat("JO", "Jordanian"), nat("JP", "Japanese"),
            nat("KE", "Kenyan"), nat("KH", "Cambodian"),
            nat("KR", "South Korean"), nat("KW", "Kuwaiti"),
            nat("KZ", "Kazakh"), nat("LB", "Lebanese"),
            nat("LU", "Luxembourger"), nat("MA", "Moroccan"),
            nat("ML", "Malian"), nat("MT", "Maltese"),
            nat("MX", "Mexican"), nat("MY", "Malaysian"),
            nat("NG", "Nigerian"), nat("NL", "Dutch"),
            nat("NO", "Norwegian"), nat("NZ", "New Zealander"),
            nat("PE", "Peruvian"), nat("PH", "Filipino"),
            nat("PK", "Pakistani"), nat("PL", "Polish"),
            nat("PT", "Portuguese"), nat("QA", "Qatari"),
            nat("RO", "Romanian"), nat("RS", "Serbian"),
            nat("RU", "Russian"), nat("SA", "Saudi"),
            nat("SE", "Swedish"), nat("SG", "Singaporean"),
            nat("SI", "Slovenian"), nat("SK", "Slovak"),
            nat("SN", "Senegalese"), nat("SY", "Syrian"),
            nat("TH", "Thai"), nat("TN", "Tunisian"),
            nat("TR", "Turkish"), nat("TW", "Taiwanese"),
            nat("UA", "Ukrainian"), nat("UG", "Ugandan"),
            nat("US", "American"), nat("VN", "Vietnamese"),
            nat("ZA", "South African")
        );
    }

    private Language lang(String code, String name) {
        Language l = new Language();
        l.setCode(code);
        l.setName(name);
        return l;
    }

    private Nationality nat(String code, String name) {
        Nationality n = new Nationality();
        n.setCode(code);
        n.setName(name);
        return n;
    }
}
