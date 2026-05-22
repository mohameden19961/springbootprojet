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
        List<Language> languages = List.of(
            new Language() {{ setCode("AR"); setName("Arabic"); }},
            new Language() {{ setCode("BN"); setName("Bengali"); }},
            new Language() {{ setCode("DE"); setName("German"); }},
            new Language() {{ setCode("EN"); setName("English"); }},
            new Language() {{ setCode("ES"); setName("Spanish"); }},
            new Language() {{ setCode("FR"); setName("French"); }},
            new Language() {{ setCode("HI"); setName("Hindi"); }},
            new Language() {{ setCode("IT"); setName("Italian"); }},
            new Language() {{ setCode("JA"); setName("Japanese"); }},
            new Language() {{ setCode("KO"); setName("Korean"); }},
            new Language() {{ setCode("NL"); setName("Dutch"); }},
            new Language() {{ setCode("PL"); setName("Polish"); }},
            new Language() {{ setCode("PT"); setName("Portuguese"); }},
            new Language() {{ setCode("RU"); setName("Russian"); }},
            new Language() {{ setCode("SV"); setName("Swedish"); }},
            new Language() {{ setCode("TR"); setName("Turkish"); }},
            new Language() {{ setCode("UK"); setName("Ukrainian"); }},
            new Language() {{ setCode("ZH"); setName("Chinese"); }}
        );

        List<Nationality> nationalities = List.of(
            new Nationality() {{ setCode("AD"); setName("Andorran"); }},
            new Nationality() {{ setCode("AE"); setName("Emirati"); }},
            new Nationality() {{ setCode("AL"); setName("Albanian"); }},
            new Nationality() {{ setCode("AR"); setName("Argentinian"); }},
            new Nationality() {{ setCode("AT"); setName("Austrian"); }},
            new Nationality() {{ setCode("AU"); setName("Australian"); }},
            new Nationality() {{ setCode("BD"); setName("Bangladeshi"); }},
            new Nationality() {{ setCode("BE"); setName("Belgian"); }},
            new Nationality() {{ setCode("BG"); setName("Bulgarian"); }},
            new Nationality() {{ setCode("BR"); setName("Brazilian"); }},
            new Nationality() {{ setCode("CA"); setName("Canadian"); }},
            new Nationality() {{ setCode("CH"); setName("Swiss"); }},
            new Nationality() {{ setCode("CI"); setName("Ivorian"); }},
            new Nationality() {{ setCode("CL"); setName("Chilean"); }},
            new Nationality() {{ setCode("CN"); setName("Chinese"); }},
            new Nationality() {{ setCode("CO"); setName("Colombian"); }},
            new Nationality() {{ setCode("CU"); setName("Cuban"); }},
            new Nationality() {{ setCode("CY"); setName("Cypriot"); }},
            new Nationality() {{ setCode("CZ"); setName("Czech"); }},
            new Nationality() {{ setCode("DE"); setName("German"); }},
            new Nationality() {{ setCode("DK"); setName("Danish"); }},
            new Nationality() {{ setCode("DZ"); setName("Algerian"); }},
            new Nationality() {{ setCode("EG"); setName("Egyptian"); }},
            new Nationality() {{ setCode("ES"); setName("Spanish"); }},
            new Nationality() {{ setCode("ET"); setName("Ethiopian"); }},
            new Nationality() {{ setCode("FI"); setName("Finnish"); }},
            new Nationality() {{ setCode("FR"); setName("French"); }},
            new Nationality() {{ setCode("GB"); setName("British"); }},
            new Nationality() {{ setCode("GH"); setName("Ghanaian"); }},
            new Nationality() {{ setCode("GR"); setName("Greek"); }},
            new Nationality() {{ setCode("HK"); setName("Hongkonger"); }},
            new Nationality() {{ setCode("HR"); setName("Croatian"); }},
            new Nationality() {{ setCode("HU"); setName("Hungarian"); }},
            new Nationality() {{ setCode("ID"); setName("Indonesian"); }},
            new Nationality() {{ setCode("IE"); setName("Irish"); }},
            new Nationality() {{ setCode("IL"); setName("Israeli"); }},
            new Nationality() {{ setCode("IN"); setName("Indian"); }},
            new Nationality() {{ setCode("IQ"); setName("Iraqi"); }},
            new Nationality() {{ setCode("IR"); setName("Iranian"); }},
            new Nationality() {{ setCode("IS"); setName("Icelander"); }},
            new Nationality() {{ setCode("IT"); setName("Italian"); }},
            new Nationality() {{ setCode("JM"); setName("Jamaican"); }},
            new Nationality() {{ setCode("JO"); setName("Jordanian"); }},
            new Nationality() {{ setCode("JP"); setName("Japanese"); }},
            new Nationality() {{ setCode("KE"); setName("Kenyan"); }},
            new Nationality() {{ setCode("KH"); setName("Cambodian"); }},
            new Nationality() {{ setCode("KR"); setName("South Korean"); }},
            new Nationality() {{ setCode("KW"); setName("Kuwaiti"); }},
            new Nationality() {{ setCode("KZ"); setName("Kazakh"); }},
            new Nationality() {{ setCode("LB"); setName("Lebanese"); }},
            new Nationality() {{ setCode("LU"); setName("Luxembourger"); }},
            new Nationality() {{ setCode("MA"); setName("Moroccan"); }},
            new Nationality() {{ setCode("ML"); setName("Malian"); }},
            new Nationality() {{ setCode("MT"); setName("Maltese"); }},
            new Nationality() {{ setCode("MX"); setName("Mexican"); }},
            new Nationality() {{ setCode("MY"); setName("Malaysian"); }},
            new Nationality() {{ setCode("NG"); setName("Nigerian"); }},
            new Nationality() {{ setCode("NL"); setName("Dutch"); }},
            new Nationality() {{ setCode("NO"); setName("Norwegian"); }},
            new Nationality() {{ setCode("NZ"); setName("New Zealander"); }},
            new Nationality() {{ setCode("PE"); setName("Peruvian"); }},
            new Nationality() {{ setCode("PH"); setName("Filipino"); }},
            new Nationality() {{ setCode("PK"); setName("Pakistani"); }},
            new Nationality() {{ setCode("PL"); setName("Polish"); }},
            new Nationality() {{ setCode("PT"); setName("Portuguese"); }},
            new Nationality() {{ setCode("QA"); setName("Qatari"); }},
            new Nationality() {{ setCode("RO"); setName("Romanian"); }},
            new Nationality() {{ setCode("RS"); setName("Serbian"); }},
            new Nationality() {{ setCode("RU"); setName("Russian"); }},
            new Nationality() {{ setCode("SA"); setName("Saudi"); }},
            new Nationality() {{ setCode("SE"); setName("Swedish"); }},
            new Nationality() {{ setCode("SG"); setName("Singaporean"); }},
            new Nationality() {{ setCode("SI"); setName("Slovenian"); }},
            new Nationality() {{ setCode("SK"); setName("Slovak"); }},
            new Nationality() {{ setCode("SN"); setName("Senegalese"); }},
            new Nationality() {{ setCode("SY"); setName("Syrian"); }},
            new Nationality() {{ setCode("TH"); setName("Thai"); }},
            new Nationality() {{ setCode("TN"); setName("Tunisian"); }},
            new Nationality() {{ setCode("TR"); setName("Turkish"); }},
            new Nationality() {{ setCode("TW"); setName("Taiwanese"); }},
            new Nationality() {{ setCode("UA"); setName("Ukrainian"); }},
            new Nationality() {{ setCode("UG"); setName("Ugandan"); }},
            new Nationality() {{ setCode("US"); setName("American"); }},
            new Nationality() {{ setCode("VN"); setName("Vietnamese"); }},
            new Nationality() {{ setCode("ZA"); setName("South African"); }}
        );

        languageRepository.saveAll(languages);
        nationalityRepository.saveAll(nationalities);

        return ResponseEntity.ok(Map.of(
            "languages", languages.size(),
            "nationalities", nationalities.size()
        ));
    }
}
