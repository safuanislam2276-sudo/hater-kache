package com.example.data.country

data class CountryInfo(
    val code: String,
    val nameBn: String,
    val nameEn: String,
    val flag: String,
    val phoneCode: String,
    val currency: String,
    val divisionLabelBn: String = "বিভাগ",
    val districtLabelBn: String = "জেলা",
    val divisionLabelEn: String = "Division",
    val districtLabelEn: String = "District",
    val divisionsBn: Map<String, List<String>> = emptyMap(),
    val divisionsEn: Map<String, List<String>> = emptyMap()
) {
    // Backward compatibility for existing code accessing .divisions
    val divisions: Map<String, List<String>>
        get() = if (divisionsBn.isNotEmpty()) divisionsBn else divisionsEn

    fun getDivisionLabel(isBn: Boolean): String = if (isBn) divisionLabelBn else divisionLabelEn
    fun getDistrictLabel(isBn: Boolean): String = if (isBn) districtLabelBn else districtLabelEn

    fun getDivisions(isBn: Boolean): Map<String, List<String>> {
        return if (isBn) {
            if (divisionsBn.isNotEmpty()) divisionsBn else divisionsEn
        } else {
            if (divisionsEn.isNotEmpty()) divisionsEn else divisionsBn
        }
    }
}

object CountryRepository {

    // Bangladesh Divisions & Districts (Bengali)
    val BD_DIVISIONS_BN = mapOf(
        "ঢাকা" to listOf("ঢাকা", "গাজীপুর", "নারায়ণগঞ্জ", "কিশোরগঞ্জ", "টাঙ্গাইল", "ফরিদপুর", "মানিকগঞ্জ", "মুন্সীগঞ্জ", "নরসিংদী", "মাদারীপুর", "গোপালগঞ্জ", "শরীয়তপুর", "রাজবাড়ী"),
        "চট্টগ্রাম" to listOf("চট্টগ্রাম", "কক্সবাজার", "কুমিল্লা", "ফেনী", "ব্রাহ্মণবাড়িয়া", "নোয়াখালী", "চাঁদপুর", "লক্ষ্মীপুর", "রাঙ্গামাটি", "বান্দরবান", "খাগড়াছড়ি"),
        "রাজশাহী" to listOf("রাজশাহী", "বগুড়া", "পাবনা", "সিরাজগঞ্জ", "নওগাঁ", "নাটোর", "চাঁপাইনবাবগঞ্জ", "জয়পুরহাট"),
        "খুলনা" to listOf("খুলনা", "যশোর", "কুষ্টিয়া", "ঝিনাইদহ", "সাতক্ষীরা", "বাগেরহাট", "চুয়াডাঙ্গা", "মেহেরপুর", "মাগুরা", "নড়াইল"),
        "বরিশাল" to listOf("বরিশাল", "পটুয়াখালী", "ভোলা", "পিরোজপুর", "বরগুনা", "ঝালকাঠি"),
        "সিলেট" to listOf("সিলেট", "মৌলভীবাজার", "হবিগঞ্জ", "সুনামগঞ্জ"),
        "রংপুর" to listOf("রংপুর", "দিনাজপুর", "কুড়িগ্রাম", "গাইবান্ধা", "নীলফামারী", "লালমনিরহাট", "ঠাকুরগাঁও", "পঞ্চগড়"),
        "ময়মনসিংহ" to listOf("ময়মনসিংহ", "জামালপুর", "নেত্রকোণা", "শেরপুর")
    )

    // Bangladesh Divisions & Districts (English)
    val BD_DIVISIONS_EN = mapOf(
        "Dhaka" to listOf("Dhaka", "Gazipur", "Narayanganj", "Kishoreganj", "Tangail", "Faridpur", "Manikganj", "Munshiganj", "Narsingdi", "Madaripur", "Gopalganj", "Shariatpur", "Rajbari"),
        "Chattogram" to listOf("Chattogram", "Cox's Bazar", "Cumilla", "Feni", "Brahmanbaria", "Noakhali", "Chandpur", "Lakshmipur", "Rangamati", "Bandarban", "Khagrachhari"),
        "Rajshahi" to listOf("Rajshahi", "Bogura", "Pabna", "Sirajganj", "Naogaon", "Natore", "Chapai Nawabganj", "Joypurhat"),
        "Khulna" to listOf("Khulna", "Jashore", "Kushtia", "Jhenaidah", "Satkhira", "Bagerhat", "Chuadanga", "Meherpur", "Magura", "Narail"),
        "Barishal" to listOf("Barishal", "Patuakhali", "Bhola", "Pirojpur", "Barguna", "Jhalokathi"),
        "Sylhet" to listOf("Sylhet", "Moulvibazar", "Habiganj", "Sunamganj"),
        "Rangpur" to listOf("Rangpur", "Dinajpur", "Kurigram", "Gaibandha", "Nilphamari", "Lalmonirhat", "Thakurgaon", "Panchagarh"),
        "Mymensingh" to listOf("Mymensingh", "Jamalpur", "Netrokona", "Sherpur")
    )

    // Saudi Arabia (100% English)
    val SAUDI_PROVINCES = mapOf(
        "Riyadh Province" to listOf("Riyadh", "Al Kharj", "Ad Diriyah", "Al Majma'ah", "Al Dawadmi", "Al Zulfi"),
        "Makkah Province" to listOf("Makkah", "Jeddah", "Taif", "Rabigh", "Al Qunfudhah", "Al Lith"),
        "Eastern Province" to listOf("Dammam", "Khobar", "Jubail", "Al Ahsa", "Qatif", "Dhahran", "Hafar Al Batin", "Ras Tanura"),
        "Madinah Province" to listOf("Madinah", "Yanbu", "Al Ula", "Badr"),
        "Asir Province" to listOf("Abha", "Khamis Mushait", "Bisha", "Mahayil"),
        "Tabuk Province" to listOf("Tabuk", "Duba", "Al Wajh", "Umluj"),
        "Al Qassim Province" to listOf("Buraidah", "Unaizah", "Al Rass"),
        "Jazan Province" to listOf("Jazan", "Sabya", "Abu Arish"),
        "Najran Province" to listOf("Najran", "Sharurah"),
        "Hail Province" to listOf("Hail", "Baqaa")
    )

    // UAE (100% English)
    val UAE_EMIRATES = mapOf(
        "Emirates" to listOf("Dubai", "Abu Dhabi", "Sharjah", "Ajman", "Ras Al Khaimah", "Fujairah", "Umm Al Quwain")
    )

    // Qatar (100% English)
    val QATAR_MUNICIPALITIES = mapOf(
        "Municipalities" to listOf("Doha", "Al Rayyan", "Al Wakrah", "Al Khor", "Umm Salal", "Al Daayen", "Al Shamal", "Al Shahaniya")
    )

    // Kuwait (100% English)
    val KUWAIT_GOVERNORATES = mapOf(
        "Governorates" to listOf("Kuwait City", "Hawalli", "Al Farwaniyah", "Al Ahmadi", "Al Jahra", "Mubarak Al-Kabeer")
    )

    // Oman (100% English)
    val OMAN_GOVERNORATES = mapOf(
        "Governorates" to listOf("Muscat", "Salalah", "Sohar", "Nizwa", "Al Buraimi", "Sur", "Seeb", "Bawshar", "Ibri", "Khasab")
    )

    // Bahrain (100% English)
    val BAHRAIN_GOVERNORATES = mapOf(
        "Governorates" to listOf("Capital (Manama)", "Muharraq", "Northern Governorate", "Southern Governorate (Riffa)", "Hamad Town", "Sitra", "A'ali", "Budaiya", "Juffair")
    )

    // India (100% English)
    val INDIA_STATES = mapOf(
        "West Bengal" to listOf("Kolkata", "Howrah", "North 24 Parganas", "South 24 Parganas", "Siliguri", "Murshidabad", "Malda", "Bardhaman", "Asansol", "Durgapur", "Nadia", "Hooghly"),
        "Delhi (NCR)" to listOf("New Delhi", "North Delhi", "South Delhi", "West Delhi", "East Delhi", "Noida", "Gurugram", "Faridabad", "Ghaziabad"),
        "Maharashtra" to listOf("Mumbai", "Pune", "Nagpur", "Thane", "Nashik", "Aurangabad", "Navi Mumbai"),
        "Karnataka" to listOf("Bengaluru", "Mysuru", "Mangaluru", "Hubballi", "Belagavi"),
        "Tamil Nadu" to listOf("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem"),
        "Telangana" to listOf("Hyderabad", "Warangal", "Nizamabad", "Karimnagar"),
        "Gujarat" to listOf("Ahmedabad", "Surat", "Vadodara", "Rajkot"),
        "Uttar Pradesh" to listOf("Lucknow", "Kanpur", "Varanasi", "Agra", "Prayagraj")
    )

    // Malaysia (100% English)
    val MALAYSIA_STATES = mapOf(
        "States & Territories" to listOf("Kuala Lumpur", "Penang", "Selangor", "Johor Bahru", "Perak", "Sabah", "Sarawak", "Melaka", "Negeri Sembilan", "Kedah", "Pahang")
    )

    // Singapore (100% English)
    val SINGAPORE_REGIONS = mapOf(
        "Regions" to listOf("Central Area", "Jurong (West)", "Tampines (East)", "Woodlands (North)", "Bedok", "Ang Mo Kio", "Yishun", "Hougang", "Choa Chu Kang", "Bukit Batok", "Pasir Ris")
    )

    // United Kingdom (100% English)
    val UK_REGIONS = mapOf(
        "England" to listOf("London", "Birmingham", "Manchester", "Leeds", "Liverpool", "Sheffield", "Bristol", "Newcastle", "Nottingham", "Leicester", "Coventry", "Bradford"),
        "Scotland" to listOf("Glasgow", "Edinburgh", "Aberdeen", "Dundee"),
        "Wales" to listOf("Cardiff", "Swansea", "Newport"),
        "Northern Ireland" to listOf("Belfast", "Derry", "Lisburn")
    )

    // United States (100% English)
    val USA_STATES = mapOf(
        "New York" to listOf("New York City", "Buffalo", "Rochester", "Yonkers", "Syracuse", "Albany"),
        "California" to listOf("Los Angeles", "San Francisco", "San Diego", "San Jose", "Fresno", "Sacramento", "Oakland", "Long Beach"),
        "Texas" to listOf("Houston", "Dallas", "Austin", "San Antonio", "Fort Worth", "El Paso", "Arlington", "Plano"),
        "Florida" to listOf("Miami", "Orlando", "Tampa", "Jacksonville", "Fort Lauderdale", "St. Petersburg", "Tallahassee"),
        "Illinois" to listOf("Chicago", "Aurora", "Naperville", "Joliet", "Rockford"),
        "Pennsylvania" to listOf("Philadelphia", "Pittsburgh", "Allentown", "Erie", "Reading"),
        "Georgia" to listOf("Atlanta", "Augusta", "Columbus", "Savannah"),
        "Michigan" to listOf("Detroit", "Grand Rapids", "Warren", "Ann Arbor"),
        "New Jersey" to listOf("Newark", "Jersey City", "Paterson", "Elizabeth"),
        "Virginia" to listOf("Virginia Beach", "Norfolk", "Chesapeake", "Richmond", "Arlington")
    )

    // Canada (100% English)
    val CANADA_PROVINCES = mapOf(
        "Ontario" to listOf("Toronto", "Ottawa", "Mississauga", "Brampton", "Hamilton", "London", "Markham", "Vaughan", "Kitchener", "Windsor"),
        "British Columbia" to listOf("Vancouver", "Victoria", "Surrey", "Burnaby", "Richmond", "Kelowna", "Coquitlam"),
        "Quebec" to listOf("Montreal", "Quebec City", "Laval", "Gatineau", "Longueuil"),
        "Alberta" to listOf("Calgary", "Edmonton", "Red Deer", "Lethbridge")
    )

    // Italy (100% English)
    val ITALY_REGIONS = mapOf(
        "Lombardy" to listOf("Milan", "Brescia", "Monza", "Bergamo", "Como"),
        "Lazio" to listOf("Rome", "Latina", "Guidonia", "Fiumicino"),
        "Veneto" to listOf("Venice", "Verona", "Padua", "Vicenza", "Treviso"),
        "Piedmont" to listOf("Turin", "Novara", "Alessandria", "Asti"),
        "Campania" to listOf("Naples", "Salerno", "Giugliano in Campania", "Caserta"),
        "Emilia-Romagna" to listOf("Bologna", "Parma", "Modena", "Reggio Emilia", "Ravenna"),
        "Tuscany" to listOf("Florence", "Prato", "Livorno", "Pisa"),
        "Sicily" to listOf("Palermo", "Catania", "Messina")
    )

    // Spain (100% English)
    val SPAIN_REGIONS = mapOf(
        "Madrid" to listOf("Madrid", "Móstoles", "Alcalá de Henares", "Fuenlabrada", "Leganés", "Getafe"),
        "Catalonia" to listOf("Barcelona", "L'Hospitalet de Llobregat", "Badalona", "Terrassa", "Sabadell", "Girona", "Tarragona", "Lleida"),
        "Andalusia" to listOf("Seville", "Malaga", "Cordoba", "Granada", "Jerez de la Frontera", "Almería", "Cadiz"),
        "Valencia" to listOf("Valencia", "Alicante", "Elche", "Castellón de la Plana")
    )

    // France (100% English)
    val FRANCE_REGIONS = mapOf(
        "Île-de-France" to listOf("Paris", "Boulogne-Billancourt", "Saint-Denis", "Argenteuil", "Montreuil", "Nanterre", "Créteil"),
        "Auvergne-Rhône-Alpes" to listOf("Lyon", "Saint-Étienne", "Grenoble", "Villeurbanne", "Clermont-Ferrand"),
        "Provence-Alpes-Côte d'Azur" to listOf("Marseille", "Nice", "Toulon", "Aix-en-Provence", "Avignon", "Cannes"),
        "Occitanie" to listOf("Toulouse", "Montpellier", "Nîmes", "Perpignan")
    )

    // Germany (100% English)
    val GERMANY_STATES = mapOf(
        "Berlin" to listOf("Berlin", "Mitte", "Pankow", "Charlottenburg"),
        "Bavaria" to listOf("Munich", "Nuremberg", "Augsburg", "Regensburg", "Ingolstadt", "Würzburg"),
        "North Rhine-Westphalia" to listOf("Cologne", "Düsseldorf", "Dortmund", "Essen", "Duisburg", "Bochum", "Wuppertal", "Bielefeld", "Bonn"),
        "Hesse" to listOf("Frankfurt", "Wiesbaden", "Kassel", "Darmstadt", "Offenbach"),
        "Baden-Württemberg" to listOf("Stuttgart", "Mannheim", "Karlsruhe", "Freiburg", "Heidelberg")
    )

    // Australia (100% English)
    val AUSTRALIA_STATES = mapOf(
        "New South Wales" to listOf("Sydney", "Newcastle", "Central Coast", "Wollongong", "Maitland", "Tweed Heads"),
        "Victoria" to listOf("Melbourne", "Geelong", "Ballarat", "Bendigo", "Shepparton"),
        "Queensland" to listOf("Brisbane", "Gold Coast", "Sunshine Coast", "Townsville", "Cairns", "Toowoomba"),
        "Western Australia" to listOf("Perth", "Mandurah", "Bunbury", "Fremantle"),
        "South Australia" to listOf("Adelaide", "Mount Gambier", "Whyalla")
    )

    // Bangladesh Upazilas (Bengali)
    val BD_UPAZILAS_BN = mapOf(
        "ঢাকা" to listOf("ধানমন্ডি", "গুলশান", "বনানী", "মিরপুর", "উত্তরা", "মতিঝিল", "মোহাম্মদপুর", "সাভার", "কেরানীগঞ্জ", "ধামরাই", "ডেমরা", "বাড্ডা", "খিলগাঁও", "পুরান ঢাকা", "তেজগাঁও", "পল্টন", "রামপুরা"),
        "গাজীপুর" to listOf("গাজীপুর সদর", "টঙ্গী", "কালিয়াকৈর", "শ্রীপুর", "কাপাসিয়া", "কালীগঞ্জ"),
        "নারায়ণগঞ্জ" to listOf("নারায়ণগঞ্জ সদর", "সিদ্ধিরগঞ্জ", "ফতুল্লা", "রূপগঞ্জ", "সোনারগাঁও", "আড়াইহাজার"),
        "চট্টগ্রাম" to listOf("কোতোয়ালী", "পতেঙ্গা", "পাঁচলাইশ", "হালিশহর", "পাহাড়তলী", "ডবলমুরিং", "চান্দগাঁও", "হাটহাজারী", "সীতাকুণ্ড", "মিরসরাই", "ফটিকছড়ি", "রাঙ্গুনিয়া", "পটিয়া", "বোয়ালখালী", "আনোয়ারা", "চন্দনাইশ", "সাতকানিয়া", "লোহাগাড়া", "বাঁশখালী", "সন্দ্বীপ"),
        "কক্সবাজার" to listOf("কক্সবাজার সদর", "চকোরিয়া", "মহেশখালী", "টেকনাফ", "উখিয়া", "রামু", "পেকুয়া", "কুতুবদিয়া"),
        "কুমিল্লা" to listOf("আদর্শ সদর", "সদর দক্ষিণ", "দাউদকান্দি", "চান্দিনা", "বুড়িচং", "ব্রাহ্মণপাড়া", "মুরাদনগর", "দেবীদ্বার", "হোমনা", "মেঘনা", "তিতাস", "বরুড়া", "লাকসাম", "মনোহরগঞ্জ", "চৌদ্দগ্রাম", "নাঙ্গলকোট"),
        "সিলেট" to listOf("সিলেট সদর", "দক্ষিণ সুরমা", "গোলাপগঞ্জ", "বিয়ানীবাজার", "বিশ্বনাথ", "ফেঞ্চুগঞ্জ", "ওসমানীনগর", "বালাগঞ্জ", "জৈন্তাপুর", "গোয়াইনঘাট", "কানাইঘাট", "কোম্পানীগঞ্জ", "জাকিরগঞ্জ"),
        "রাজশাহী" to listOf("বোয়ালিয়া", "রাজপাড়া", "মতিহার", "শাহ মখদুম", "পবা", "গোদাগাড়ী", "তানোর", "মোহনপুর", "বাগমারা", "দুর্গাপুর", "পুঠিয়া", "চারঘাট", "বাঘা"),
        "বগুড়া" to listOf("বগুড়া সদর", "শাজাহানপুর", "শেরপুর", "শিবগঞ্জ", "দুপচাঁচিয়া", "আদমদীঘি", "কাহালু", "নন্দীগ্রাম", "ধুনট", "গাবতলী", "সারিয়াকান্দি", "সোনাতলা"),
        "খুলনা" to listOf("খুলনা সদর", "সোনাডাঙ্গা", "খালিশপুর", "দৌলতপুর", "খান জাহান আলী", "ডুমুরিয়া", "বটিয়াঘাটা", "রূপসা", "তেরখাদা", "দিঘলিয়া", "পাইকগাছা", "কয়রা", "দাকোপ", "ফুলতলা"),
        "যশোর" to listOf("যশোর সদর", "ঝিকরগাছা", "শার্শা", "মণিরামপুর", "চৌগাছা", "অভয়নগর", "বাঘারপাড়া", "কেশবপুর"),
        "বরিশাল" to listOf("কোতোয়ালী", "বরিশাল সদর", "বাবুগঞ্জ", "উজিরপুর", "গৌরনদী", "আগৈলঝাড়া", "বানারীপাড়া", "বাকেরগঞ্জ", "মেহেন্দিগঞ্জ", "মুলাদী", "হিজলা"),
        "রংপুর" to listOf("রংপুর সদর", "গঙ্গাচড়া", "তারাগঞ্জ", "বদরগঞ্জ", "মিঠাপুকুর", "পীরগাছা", "কাউনিয়া", "পীরগঞ্জ"),
        "ময়মনসিংহ" to listOf("ময়মনসিংহ সদর", "মুক্তাগাছা", "ত্রিশাল", "ভালুকা", "গফরগাঁও", "ফুলবাড়িয়া", "ঈশ্বরগঞ্জ", "নান্দাইল", "গৌরীপুর", "হালুয়াঘাট", "ধোবাউড়া", "ফুলপুর", "তারাকান্দা")
    )

    // Bangladesh Upazilas (English)
    val BD_UPAZILAS_EN = mapOf(
        "Dhaka" to listOf("Dhanmondi", "Gulshan", "Banani", "Mirpur", "Uttara", "Motijheel", "Mohammadpur", "Savar", "Keraniganj", "Dhamrai", "Demra", "Badda", "Khilgaon", "Old Dhaka", "Tejgaon", "Paltan", "Rampura"),
        "Gazipur" to listOf("Gazipur Sadar", "Tongi", "Kaliakair", "Sreepur", "Kapasia", "Kaliganj"),
        "Narayanganj" to listOf("Narayanganj Sadar", "Siddhirganj", "Fatullah", "Rupganj", "Sonargaon", "Araihazar"),
        "Chattogram" to listOf("Kotwali", "Patenga", "Panchlaish", "Halishahar", "Pahartali", "Double Mooring", "Chandgaon", "Hathazari", "Sitakunda", "Mirsharai", "Fatikchhari", "Rangunia", "Patiya", "Boalkhali", "Anwara", "Chandanaiash", "Satkania", "Lohagara", "Banshkhali", "Sandwip"),
        "Cox's Bazar" to listOf("Cox's Bazar Sadar", "Chakaria", "Maheshkhali", "Teknaf", "Ukhia", "Ramu", "Pekua", "Kutubdia"),
        "Cumilla" to listOf("Adarsha Sadar", "Sadar South", "Daudkandi", "Chandina", "Burichang", "Brahmanpara", "Muradnagar", "Debidwar", "Homna", "Meghna", "Titas", "Barura", "Laksam", "Manoharganj", "Chauddagram", "Nangalkot"),
        "Sylhet" to listOf("Sylhet Sadar", "South Surma", "Golapganj", "Beanibazar", "Bishwanath", "Fenchuganj", "Osmani Nagar", "Balaganj", "Jaintiapur", "Gowainghat", "Kanaighat", "Companiganj", "Zakiganj"),
        "Rajshahi" to listOf("Boalia", "Rajpara", "Motihar", "Shah Makhdum", "Paba", "Godagari", "Tanore", "Mohanpur", "Bagmara", "Durgapur", "Puthia", "Charghat", "Bagha"),
        "Bogura" to listOf("Bogura Sadar", "Shajahanpur", "Sherpur", "Shibganj", "Dupchanchia", "Adamdighi", "Kahalu", "Nandigram", "Dhunat", "Gabtali", "Sariakandi", "Sonatala"),
        "Khulna" to listOf("Khulna Sadar", "Sonadanga", "Khalishpur", "Daulatpur", "Khan Jahan Ali", "Dumuria", "Batiaghata", "Rupsha", "Terokhada", "Dighalia", "Paikgachha", "Koyra", "Dacope", "Phultala"),
        "Jashore" to listOf("Jashore Sadar", "Jhikargachha", "Sharsha", "Manirampur", "Chaugachha", "Abhaynagar", "Bagherpara", "Keshabpur"),
        "Barishal" to listOf("Kotwali", "Barishal Sadar", "Babuganj", "Wazirpur", "Gournadi", "Agailjhara", "Banaripara", "Bakerganj", "Mehendiganj", "Muladi", "Hizla"),
        "Rangpur" to listOf("Rangpur Sadar", "Gangachhara", "Taraganj", "Badarganj", "Mithapukur", "Pirgachha", "Kaunia", "Pirganj"),
        "Mymensingh" to listOf("Mymensingh Sadar", "Muktagachha", "Trishal", "Bhaluka", "Gafargaon", "Fulbaria", "Ishwarganj", "Nandail", "Gouripur", "Haluaghat", "Dhobaura", "Fulpur", "Tarakanda")
    )

    // International areas for major foreign cities
    val INTERNATIONAL_AREAS = mapOf(
        "Dubai" to listOf("Downtown Dubai", "Dubai Marina", "Deira", "Bur Dubai", "Jumeirah", "Business Bay", "Al Barsha", "Palm Jumeirah", "Al Quoz", "International City", "Al Nahda"),
        "Abu Dhabi" to listOf("Al Danah", "Al Zahiyah", "Al Reem Island", "Corniche", "Khalidiya", "Mussafah", "Yas Island", "Mohammed Bin Zayed City"),
        "Sharjah" to listOf("Al Majaz", "Al Nahda", "Al Taawun", "Al Qasimia", "Muwaileh", "Rolla", "Al Khan"),
        "Riyadh" to listOf("Al Olaya", "Al Malaz", "Al Nakheel", "Al Sulaimaniyah", "Al Murabba", "Al Batha", "Al Shifa", "Al Naseem", "Diplomatic Quarter"),
        "Jeddah" to listOf("Al Balad", "Al Hamra", "Al Rawdah", "Al Zahra", "Al Safa", "Al Bawadi", "Al Marwah", "Al Shate'a", "Obhur"),
        "Makkah" to listOf("Al Haram Area", "Aziziyah", "Al Maabdah", "Al Shoqiyah", "Al Kakiyyah", "Al Rusaifah"),
        "Madinah" to listOf("Central Area (Markaziyah)", "Quba", "Al Qiblatain", "Al Khalidiyyah", "Al Uyoon"),
        "Dammam" to listOf("Al Faisaliyah", "Al Shatie", "Al Mazruiyah", "Al Jalawiyah", "Al Rakah"),
        "Khobar" to listOf("Al Khobar Al Shamalia", "Al Khobar Al Janubia", "Al Rakah", "Al Thuqbah", "Al Jisr"),
        "Doha" to listOf("West Bay", "The Pearl", "Al Sadd", "Al Mansoura", "Najma", "Old Airport", "Msheireb Downtown", "Al Dafna"),
        "Kuwait City" to listOf("Sharq", "Mirqab", "Salhiya", "Qibla", "Dasman", "Bneid Al Gar"),
        "Hawalli" to listOf("Hawalli City", "Salmiya", "Jabriya", "Rumaithiya", "Bayan", "Mishref"),
        "Muscat" to listOf("Muttrah", "Ruwi", "Al Khuwair", "Qurum", "Al Ghubrah", "Al Athaiba", "Seeb", "Bawshar", "Al Mawaleh"),
        "Capital (Manama)" to listOf("Juffair", "Seef", "Hoora", "Gudaibiya", "Adliya", "Diplomatic Area", "Zinj"),
        "London" to listOf("City of London", "Westminster", "Camden", "Kensington & Chelsea", "Greenwich", "Hackney", "Tower Hamlets", "Newham", "Croydon", "Ealing"),
        "New York City" to listOf("Manhattan", "Brooklyn", "Queens", "Bronx", "Staten Island"),
        "Toronto" to listOf("Downtown", "North York", "Scarborough", "Etobicoke", "York", "East York"),
        "Kolkata" to listOf("Salt Lake", "New Town", "Park Street", "Ballygunge", "Howrah", "Dum Dum", "Garia", "Behala", "Jadavpur"),
        "Kuala Lumpur" to listOf("KLCC / City Centre", "Bukit Bintang", "Bangsar", "Mont Kiara", "Cheras", "Setapak", "Kepong", "Brickfields"),
        "Central Area" to listOf("Marina Bay", "Orchard Road", "Chinatown", "Bugis", "Raffles Place", "Tanjong Pagar")
    )

    // Primary countries with detailed divisions
    // For BD: Bilingual. For all other countries: Pure English for province/district labels and lists
    val PRIMARY_COUNTRIES = listOf(
        CountryInfo(
            code = "BD",
            nameBn = "বাংলাদেশ",
            nameEn = "Bangladesh",
            flag = "🇧🇩",
            phoneCode = "+880",
            currency = "৳",
            divisionLabelBn = "বিভাগ",
            districtLabelBn = "জেলা",
            divisionLabelEn = "Division",
            districtLabelEn = "District",
            divisionsBn = BD_DIVISIONS_BN,
            divisionsEn = BD_DIVISIONS_EN
        ),
        CountryInfo(
            code = "SA",
            nameBn = "সৌদি আরব",
            nameEn = "Saudi Arabia",
            flag = "🇸🇦",
            phoneCode = "+966",
            currency = "SAR",
            divisionLabelBn = "Province",
            districtLabelBn = "City",
            divisionLabelEn = "Province",
            districtLabelEn = "City",
            divisionsBn = SAUDI_PROVINCES,
            divisionsEn = SAUDI_PROVINCES
        ),
        CountryInfo(
            code = "AE",
            nameBn = "সংযুক্ত আরব আমিরাত",
            nameEn = "UAE",
            flag = "🇦🇪",
            phoneCode = "+971",
            currency = "AED",
            divisionLabelBn = "Emirate",
            districtLabelBn = "Area / City",
            divisionLabelEn = "Emirate",
            districtLabelEn = "Area / City",
            divisionsBn = UAE_EMIRATES,
            divisionsEn = UAE_EMIRATES
        ),
        CountryInfo(
            code = "QA",
            nameBn = "কাতার",
            nameEn = "Qatar",
            flag = "🇶🇦",
            phoneCode = "+974",
            currency = "QAR",
            divisionLabelBn = "Municipality",
            districtLabelBn = "City / Zone",
            divisionLabelEn = "Municipality",
            districtLabelEn = "City / Zone",
            divisionsBn = QATAR_MUNICIPALITIES,
            divisionsEn = QATAR_MUNICIPALITIES
        ),
        CountryInfo(
            code = "KW",
            nameBn = "কুয়েত",
            nameEn = "Kuwait",
            flag = "🇰🇼",
            phoneCode = "+965",
            currency = "KWD",
            divisionLabelBn = "Governorate",
            districtLabelBn = "City / Area",
            divisionLabelEn = "Governorate",
            districtLabelEn = "City / Area",
            divisionsBn = KUWAIT_GOVERNORATES,
            divisionsEn = KUWAIT_GOVERNORATES
        ),
        CountryInfo(
            code = "OM",
            nameBn = "ওমান",
            nameEn = "Oman",
            flag = "🇴🇲",
            phoneCode = "+968",
            currency = "OMR",
            divisionLabelBn = "Governorate",
            districtLabelBn = "City / Wilayat",
            divisionLabelEn = "Governorate",
            districtLabelEn = "City / Wilayat",
            divisionsBn = OMAN_GOVERNORATES,
            divisionsEn = OMAN_GOVERNORATES
        ),
        CountryInfo(
            code = "BH",
            nameBn = "বাহরাইন",
            nameEn = "Bahrain",
            flag = "🇧🇭",
            phoneCode = "+973",
            currency = "BHD",
            divisionLabelBn = "Governorate",
            districtLabelBn = "City / Town",
            divisionLabelEn = "Governorate",
            districtLabelEn = "City / Town",
            divisionsBn = BAHRAIN_GOVERNORATES,
            divisionsEn = BAHRAIN_GOVERNORATES
        ),
        CountryInfo(
            code = "IN",
            nameBn = "ভারত",
            nameEn = "India",
            flag = "🇮🇳",
            phoneCode = "+91",
            currency = "₹",
            divisionLabelBn = "State",
            districtLabelBn = "District / City",
            divisionLabelEn = "State",
            districtLabelEn = "District / City",
            divisionsBn = INDIA_STATES,
            divisionsEn = INDIA_STATES
        ),
        CountryInfo(
            code = "MY",
            nameBn = "মালয়েশিয়া",
            nameEn = "Malaysia",
            flag = "🇲🇾",
            phoneCode = "+60",
            currency = "MYR",
            divisionLabelBn = "State",
            districtLabelBn = "City / District",
            divisionLabelEn = "State",
            districtLabelEn = "City / District",
            divisionsBn = MALAYSIA_STATES,
            divisionsEn = MALAYSIA_STATES
        ),
        CountryInfo(
            code = "SG",
            nameBn = "সিঙ্গাপুর",
            nameEn = "Singapore",
            flag = "🇸🇬",
            phoneCode = "+65",
            currency = "SGD",
            divisionLabelBn = "Region",
            districtLabelBn = "Town / Area",
            divisionLabelEn = "Region",
            districtLabelEn = "Town / Area",
            divisionsBn = SINGAPORE_REGIONS,
            divisionsEn = SINGAPORE_REGIONS
        ),
        CountryInfo(
            code = "GB",
            nameBn = "যুক্তরাজ্য",
            nameEn = "United Kingdom",
            flag = "🇬🇧",
            phoneCode = "+44",
            currency = "£",
            divisionLabelBn = "Region",
            districtLabelBn = "City / Borough",
            divisionLabelEn = "Region",
            districtLabelEn = "City / Borough",
            divisionsBn = UK_REGIONS,
            divisionsEn = UK_REGIONS
        ),
        CountryInfo(
            code = "US",
            nameBn = "যুক্তরাষ্ট্র",
            nameEn = "United States",
            flag = "🇺🇸",
            phoneCode = "+1",
            currency = "$",
            divisionLabelBn = "State",
            districtLabelBn = "City / Metro",
            divisionLabelEn = "State",
            districtLabelEn = "City / Metro",
            divisionsBn = USA_STATES,
            divisionsEn = USA_STATES
        ),
        CountryInfo(
            code = "CA",
            nameBn = "কানাডা",
            nameEn = "Canada",
            flag = "🇨🇦",
            phoneCode = "+1",
            currency = "CAD",
            divisionLabelBn = "Province",
            districtLabelBn = "City / Metro",
            divisionLabelEn = "Province",
            districtLabelEn = "City / Metro",
            divisionsBn = CANADA_PROVINCES,
            divisionsEn = CANADA_PROVINCES
        ),
        CountryInfo(
            code = "IT",
            nameBn = "ইতালি",
            nameEn = "Italy",
            flag = "🇮🇹",
            phoneCode = "+39",
            currency = "€",
            divisionLabelBn = "Region",
            districtLabelBn = "City / Province",
            divisionLabelEn = "Region",
            districtLabelEn = "City / Province",
            divisionsBn = ITALY_REGIONS,
            divisionsEn = ITALY_REGIONS
        ),
        CountryInfo(
            code = "ES",
            nameBn = "স্পেন",
            nameEn = "Spain",
            flag = "🇪🇸",
            phoneCode = "+34",
            currency = "€",
            divisionLabelBn = "Region",
            districtLabelBn = "City / Province",
            divisionLabelEn = "Region",
            districtLabelEn = "City / Province",
            divisionsBn = SPAIN_REGIONS,
            divisionsEn = SPAIN_REGIONS
        ),
        CountryInfo(
            code = "FR",
            nameBn = "ফ্রান্স",
            nameEn = "France",
            flag = "🇫🇷",
            phoneCode = "+33",
            currency = "€",
            divisionLabelBn = "Region",
            districtLabelBn = "City / Department",
            divisionLabelEn = "Region",
            districtLabelEn = "City / Department",
            divisionsBn = FRANCE_REGIONS,
            divisionsEn = FRANCE_REGIONS
        ),
        CountryInfo(
            code = "DE",
            nameBn = "জার্মানি",
            nameEn = "Germany",
            flag = "🇩🇪",
            phoneCode = "+49",
            currency = "€",
            divisionLabelBn = "State",
            districtLabelBn = "City / District",
            divisionLabelEn = "State",
            districtLabelEn = "City / District",
            divisionsBn = GERMANY_STATES,
            divisionsEn = GERMANY_STATES
        ),
        CountryInfo(
            code = "AU",
            nameBn = "অস্ট্রেলিয়া",
            nameEn = "Australia",
            flag = "🇦🇺",
            phoneCode = "+61",
            currency = "AUD",
            divisionLabelBn = "State",
            districtLabelBn = "City / Region",
            divisionLabelEn = "State",
            districtLabelEn = "City / Region",
            divisionsBn = AUSTRALIA_STATES,
            divisionsEn = AUSTRALIA_STATES
        )
    )

    // Global additional countries covering global list
    // Global additional countries covering global list (82 countries + 18 primary = exactly 100 countries)
    // Every single country is complete with its real administrative divisions and major cities/districts.
    val ADDITIONAL_COUNTRIES = listOf(
        CountryInfo("PT", "পর্তুগাল", "Portugal", "🇵🇹", "+351", "€", "Region", "City", "Region", "City", GlobalCountryDivisions.PORTUGAL_MAP, GlobalCountryDivisions.PORTUGAL_MAP),
        CountryInfo("GR", "গ্রিস", "Greece", "🇬🇷", "+30", "€", "Region", "City", "Region", "City", GlobalCountryDivisions.GREECE_MAP, GlobalCountryDivisions.GREECE_MAP),
        CountryInfo("NZ", "নিউজিল্যান্ড", "New Zealand", "🇳🇿", "+64", "NZD", "Region", "City", "Region", "City", GlobalCountryDivisions.NEW_ZEALAND_MAP, GlobalCountryDivisions.NEW_ZEALAND_MAP),
        CountryInfo("JP", "জাপান", "Japan", "🇯🇵", "+81", "¥", "Prefecture", "City", "Prefecture", "City", GlobalCountryDivisions.JAPAN_MAP, GlobalCountryDivisions.JAPAN_MAP),
        CountryInfo("KR", "দক্ষিণ কোরিয়া", "South Korea", "🇰🇷", "+82", "₩", "Province", "City", "Province", "City", GlobalCountryDivisions.SOUTH_KOREA_MAP, GlobalCountryDivisions.SOUTH_KOREA_MAP),
        CountryInfo("TR", "তুরস্ক", "Turkey", "🇹🇷", "+90", "TRY", "Province", "City", "Province", "City", GlobalCountryDivisions.TURKEY_MAP, GlobalCountryDivisions.TURKEY_MAP),
        CountryInfo("EG", "মিশর", "Egypt", "🇪🇬", "+20", "EGP", "Governorate", "City", "Governorate", "City", GlobalCountryDivisions.EGYPT_MAP, GlobalCountryDivisions.EGYPT_MAP),
        CountryInfo("ZA", "দক্ষিণ আফ্রিকা", "South Africa", "🇿🇦", "+27", "ZAR", "Province", "City", "Province", "City", GlobalCountryDivisions.SOUTH_AFRICA_MAP, GlobalCountryDivisions.SOUTH_AFRICA_MAP),
        CountryInfo("MV", "মালদ্বীপ", "Maldives", "🇲🇻", "+960", "MVR", "Atoll", "Island", "Atoll", "Island", GlobalCountryDivisions.MALDIVES_MAP, GlobalCountryDivisions.MALDIVES_MAP),
        CountryInfo("LK", "শ্রীলঙ্কা", "Sri Lanka", "🇱🇰", "+94", "LKR", "Province", "District", "Province", "District", GlobalCountryDivisions.SRI_LANKA_MAP, GlobalCountryDivisions.SRI_LANKA_MAP),
        CountryInfo("NP", "নেপাল", "Nepal", "🇳🇵", "+977", "NPR", "Province", "District", "Province", "District", GlobalCountryDivisions.NEPAL_MAP, GlobalCountryDivisions.NEPAL_MAP),
        CountryInfo("PK", "পাকিস্তান", "Pakistan", "🇵🇰", "+92", "PKR", "Province", "City", "Province", "City", GlobalCountryDivisions.PAKISTAN_MAP, GlobalCountryDivisions.PAKISTAN_MAP),
        CountryInfo("TH", "থাইল্যান্ড", "Thailand", "🇹🇭", "+66", "THB", "Province", "District", "Province", "District", GlobalCountryDivisions.THAILAND_MAP, GlobalCountryDivisions.THAILAND_MAP),
        CountryInfo("ID", "ইন্দোনেশিয়া", "Indonesia", "🇮🇩", "+62", "IDR", "Province", "City", "Province", "City", GlobalCountryDivisions.INDONESIA_MAP, GlobalCountryDivisions.INDONESIA_MAP),
        CountryInfo("VN", "ভিয়েতনাম", "Vietnam", "🇻🇳", "+84", "VND", "Province", "City", "Province", "City", GlobalCountryDivisions.VIETNAM_MAP, GlobalCountryDivisions.VIETNAM_MAP),
        CountryInfo("PH", "ফিলিপাইন", "Philippines", "🇵🇭", "+63", "PHP", "Region", "City", "Region", "City", GlobalCountryDivisions.PHILIPPINES_MAP, GlobalCountryDivisions.PHILIPPINES_MAP),
        CountryInfo("BN", "ব্রুনাই", "Brunei", "🇧🇳", "+673", "BND", "District", "Mukim", "District", "Mukim", GlobalCountryDivisions.BRUNEI_MAP, GlobalCountryDivisions.BRUNEI_MAP),
        CountryInfo("CN", "চীন", "China", "🇨🇳", "+86", "CNY", "Province", "City", "Province", "City", GlobalCountryDivisions.CHINA_MAP, GlobalCountryDivisions.CHINA_MAP),
        CountryInfo("RU", "রাশিয়া", "Russia", "🇷🇺", "+7", "RUB", "Federal Subject", "City", "Federal Subject", "City", GlobalCountryDivisions.RUSSIA_MAP, GlobalCountryDivisions.RUSSIA_MAP),
        CountryInfo("BR", "ব্রাজিল", "Brazil", "🇧🇷", "+55", "BRL", "State", "City", "State", "City", GlobalCountryDivisions.BRAZIL_MAP, GlobalCountryDivisions.BRAZIL_MAP),
        CountryInfo("AR", "আর্জেন্টিনা", "Argentina", "🇦🇷", "+54", "ARS", "Province", "City", "Province", "City", GlobalCountryDivisions.ARGENTINA_MAP, GlobalCountryDivisions.ARGENTINA_MAP),
        CountryInfo("MX", "মেক্সিকো", "Mexico", "🇲🇽", "+52", "MXN", "State", "City", "State", "City", GlobalCountryDivisions.MEXICO_MAP, GlobalCountryDivisions.MEXICO_MAP),
        CountryInfo("SE", "সুইডেন", "Sweden", "🇸🇪", "+46", "SEK", "County", "Municipality", "County", "Municipality", GlobalCountryDivisions.SWEDEN_MAP, GlobalCountryDivisions.SWEDEN_MAP),
        CountryInfo("NO", "নরওয়ে", "Norway", "🇳🇴", "+47", "NOK", "County", "Municipality", "County", "Municipality", GlobalCountryDivisions.NORWAY_MAP, GlobalCountryDivisions.NORWAY_MAP),
        CountryInfo("DK", "ডেনমার্ক", "Denmark", "🇩🇰", "+45", "DKK", "Region", "Municipality", "Region", "Municipality", GlobalCountryDivisions.DENMARK_MAP, GlobalCountryDivisions.DENMARK_MAP),
        CountryInfo("FI", "ফিনল্যান্ড", "Finland", "🇫🇮", "+358", "€", "Region", "City", "Region", "City", GlobalCountryDivisions.FINLAND_MAP, GlobalCountryDivisions.FINLAND_MAP),
        CountryInfo("NL", "নেদারল্যান্ডস", "Netherlands", "🇳🇱", "+31", "€", "Province", "City", "Province", "City", GlobalCountryDivisions.NETHERLANDS_MAP, GlobalCountryDivisions.NETHERLANDS_MAP),
        CountryInfo("BE", "বেলজিয়াম", "Belgium", "🇧🇪", "+32", "€", "Region", "City", "Region", "City", GlobalCountryDivisions.BELGIUM_MAP, GlobalCountryDivisions.BELGIUM_MAP),
        CountryInfo("CH", "সুইজারল্যান্ড", "Switzerland", "🇨🇭", "+41", "CHF", "Canton", "City", "Canton", "City", GlobalCountryDivisions.SWITZERLAND_MAP, GlobalCountryDivisions.SWITZERLAND_MAP),
        CountryInfo("AT", "অস্ট্রিয়া", "Austria", "🇦🇹", "+43", "€", "State", "City", "State", "City", GlobalCountryDivisions.AUSTRIA_MAP, GlobalCountryDivisions.AUSTRIA_MAP),
        CountryInfo("IE", "আয়ারল্যান্ড", "Ireland", "🇮🇪", "+353", "€", "County", "City", "County", "City", GlobalCountryDivisions.IRELAND_MAP, GlobalCountryDivisions.IRELAND_MAP),
        CountryInfo("PL", "পোল্যান্ড", "Poland", "🇵🇱", "+48", "PLN", "Voivodeship", "City", "Voivodeship", "City", GlobalCountryDivisions.POLAND_MAP, GlobalCountryDivisions.POLAND_MAP),
        CountryInfo("RO", "রোমানিয়া", "Romania", "🇷🇴", "+40", "RON", "County", "City", "County", "City", GlobalCountryDivisions.ROMANIA_MAP, GlobalCountryDivisions.ROMANIA_MAP),
        CountryInfo("JO", "জর্ডান", "Jordan", "🇯🇴", "+962", "JOD", "Governorate", "City", "Governorate", "City", GlobalCountryDivisions.JORDAN_MAP, GlobalCountryDivisions.JORDAN_MAP),
        CountryInfo("LB", "লেবানন", "Lebanon", "🇱🇧", "+961", "LBP", "Governorate", "City", "Governorate", "City", GlobalCountryDivisions.LEBANON_MAP, GlobalCountryDivisions.LEBANON_MAP),
        CountryInfo("IQ", "ইরাক", "Iraq", "🇮🇶", "+964", "IQD", "Governorate", "City", "Governorate", "City", GlobalCountryDivisions.IRAQ_MAP, GlobalCountryDivisions.IRAQ_MAP),
        CountryInfo("LY", "লিবিয়া", "Libya", "🇱🇾", "+218", "LYD", "District", "City", "District", "City", GlobalCountryDivisions.LIBYA_MAP, GlobalCountryDivisions.LIBYA_MAP),
        CountryInfo("CY", "সাইপ্রাস", "Cyprus", "🇨🇾", "+357", "€", "District", "City", "District", "City", GlobalCountryDivisions.CYPRUS_MAP, GlobalCountryDivisions.CYPRUS_MAP),
        CountryInfo("MA", "মরক্কো", "Morocco", "🇲🇦", "+212", "MAD", "Region", "City", "Region", "City", GlobalCountryDivisions.MOROCCO_MAP, GlobalCountryDivisions.MOROCCO_MAP),
        CountryInfo("DZ", "আলজেরিয়া", "Algeria", "🇩🇿", "+213", "DZD", "Province", "City", "Province", "City", GlobalCountryDivisions.ALGERIA_MAP, GlobalCountryDivisions.ALGERIA_MAP),
        CountryInfo("TN", "তিউনিসিয়া", "Tunisia", "🇹🇳", "+216", "TND", "Governorate", "City", "Governorate", "City", GlobalCountryDivisions.TUNISIA_MAP, GlobalCountryDivisions.TUNISIA_MAP),
        CountryInfo("KE", "কেনিয়া", "Kenya", "🇰🇪", "+254", "KES", "County", "City", "County", "City", GlobalCountryDivisions.KENYA_MAP, GlobalCountryDivisions.KENYA_MAP),
        CountryInfo("NG", "নাইজেরিয়া", "Nigeria", "🇳🇬", "+234", "NGN", "State", "City", "State", "City", GlobalCountryDivisions.NIGERIA_MAP, GlobalCountryDivisions.NIGERIA_MAP),
        CountryInfo("GH", "ঘানা", "Ghana", "🇬🇭", "+233", "GHS", "Region", "City", "Region", "City", GlobalCountryDivisions.GHANA_MAP, GlobalCountryDivisions.GHANA_MAP),
        CountryInfo("CL", "চিলি", "Chile", "🇨🇱", "+56", "CLP", "Region", "City", "Region", "City", GlobalCountryDivisions.CHILE_MAP, GlobalCountryDivisions.CHILE_MAP),
        CountryInfo("CO", "কলম্বিয়া", "Colombia", "🇨🇴", "+57", "COP", "Department", "City", "Department", "City", GlobalCountryDivisions.COLOMBIA_MAP, GlobalCountryDivisions.COLOMBIA_MAP),
        CountryInfo("PE", "পেরু", "Peru", "🇵🇪", "+51", "PEN", "Region", "City", "Region", "City", GlobalCountryDivisions.PERU_MAP, GlobalCountryDivisions.PERU_MAP),
        CountryInfo("EC", "ইকুয়েডর", "Ecuador", "🇪🇨", "+593", "USD", "Province", "City", "Province", "City", GlobalCountryDivisions.ECUADOR_MAP, GlobalCountryDivisions.ECUADOR_MAP),
        CountryInfo("VE", "ভেনিজুয়েলা", "Venezuela", "🇻🇪", "+58", "VES", "State", "City", "State", "City", GlobalCountryDivisions.VENEZUELA_MAP, GlobalCountryDivisions.VENEZUELA_MAP),
        CountryInfo("BO", "বলিভিয়া", "Bolivia", "🇧🇴", "+591", "BOB", "Department", "City", "Department", "City", GlobalCountryDivisions.BOLIVIA_MAP, GlobalCountryDivisions.BOLIVIA_MAP),
        CountryInfo("UY", "উরুগুয়ে", "Uruguay", "ইউওয়াই", "+598", "UYU", "Department", "City", "Department", "City", GlobalCountryDivisions.URUGUAY_MAP, GlobalCountryDivisions.URUGUAY_MAP),
        CountryInfo("PY", "প্যারাগুয়ে", "Paraguay", "🇵🇾", "+595", "PYG", "Department", "City", "Department", "City", GlobalCountryDivisions.PARAGUAY_MAP, GlobalCountryDivisions.PARAGUAY_MAP),
        CountryInfo("PA", "পানামা", "Panama", "🇵🇦", "+507", "PAB", "Province", "City", "Province", "City", GlobalCountryDivisions.PANAMA_MAP, GlobalCountryDivisions.PANAMA_MAP),
        CountryInfo("CR", "কোস্টারিকা", "Costa Rica", "🇨🇷", "+506", "CRC", "Province", "City", "Province", "City", GlobalCountryDivisions.COSTA_RICA_MAP, GlobalCountryDivisions.COSTA_RICA_MAP),
        CountryInfo("GT", "গুয়াতেমালা", "Guatemala", "🇬🇹", "+502", "GTQ", "Department", "City", "Department", "City", GlobalCountryDivisions.GUATEMALA_MAP, GlobalCountryDivisions.GUATEMALA_MAP),
        CountryInfo("CU", "কিউবা", "Cuba", "🇨🇺", "+53", "CUP", "Province", "City", "Province", "City", GlobalCountryDivisions.CUBA_MAP, GlobalCountryDivisions.CUBA_MAP),
        CountryInfo("JM", "জামাইকা", "Jamaica", "🇯🇲", "+1876", "JMD", "Parish", "City", "Parish", "City", GlobalCountryDivisions.JAMAICA_MAP, GlobalCountryDivisions.JAMAICA_MAP),
        CountryInfo("BS", "বাহামাস", "Bahamas", "🇧🇸", "+1242", "BSD", "District", "City", "District", "City", GlobalCountryDivisions.BAHAMAS_MAP, GlobalCountryDivisions.BAHAMAS_MAP),
        CountryInfo("BB", "বার্বাডোজ", "Barbados", "🇧🇧", "+1246", "BBD", "Parish", "City", "Parish", "City", GlobalCountryDivisions.BARBADOS_MAP, GlobalCountryDivisions.BARBADOS_MAP),
        CountryInfo("CZ", "চেক প্রজাতন্ত্র", "Czech Republic", "🇨🇿", "+420", "CZK", "Region", "City", "Region", "City", GlobalCountryDivisions.CZECH_MAP, GlobalCountryDivisions.CZECH_MAP),
        CountryInfo("HU", "হাঙ্গেরি", "Hungary", "🇭🇺", "+36", "HUF", "County", "City", "County", "City", GlobalCountryDivisions.HUNGARY_MAP, GlobalCountryDivisions.HUNGARY_MAP),
        CountryInfo("UA", "ইউক্রেন", "Ukraine", "🇺🇦", "+380", "UAH", "Oblast", "City", "Oblast", "City", GlobalCountryDivisions.UKRAINE_MAP, GlobalCountryDivisions.UKRAINE_MAP),
        CountryInfo("BY", "বেলারুশ", "Belarus", "🇧🇾", "+375", "BYN", "Oblast", "City", "Oblast", "City", GlobalCountryDivisions.BELARUS_MAP, GlobalCountryDivisions.BELARUS_MAP),
        CountryInfo("BG", "বুলগেরিয়া", "Bulgaria", "🇧🇬", "+359", "BGN", "Province", "City", "Province", "City", GlobalCountryDivisions.BULGARIA_MAP, GlobalCountryDivisions.BULGARIA_MAP),
        CountryInfo("RS", "সার্বিয়া", "Serbia", "🇷🇸", "+381", "RSD", "District", "City", "District", "City", GlobalCountryDivisions.SERBIA_MAP, GlobalCountryDivisions.SERBIA_MAP),
        CountryInfo("HR", "ক্রোয়েশিয়া", "Croatia", "🇭🇷", "+385", "€", "County", "City", "County", "City", GlobalCountryDivisions.CROATIA_MAP, GlobalCountryDivisions.CROATIA_MAP),
        CountryInfo("SK", "স্লোভাকিয়া", "Slovakia", "🇸🇰", "+421", "€", "Region", "City", "Region", "City", GlobalCountryDivisions.SLOVAKIA_MAP, GlobalCountryDivisions.SLOVAKIA_MAP),
        CountryInfo("SI", "স্লোভেনিয়া", "Slovenia", "🇸🇮", "+386", "€", "Region", "City", "Region", "City", GlobalCountryDivisions.SLOVENIA_MAP, GlobalCountryDivisions.SLOVENIA_MAP),
        CountryInfo("BA", "বসনিয়া ও হার্জেগোভিনা", "Bosnia", "🇧🇦", "+387", "BAM", "Canton", "City", "Canton", "City", GlobalCountryDivisions.BOSNIA_MAP, GlobalCountryDivisions.BOSNIA_MAP),
        CountryInfo("EE", "এস্তোনিয়া", "Estonia", "🇪🇪", "+372", "€", "County", "City", "County", "City", GlobalCountryDivisions.ESTONIA_MAP, GlobalCountryDivisions.ESTONIA_MAP),
        CountryInfo("LV", "লাটভিয়া", "Latvia", "🇱🇻", "+371", "€", "Municipality", "City", "Municipality", "City", GlobalCountryDivisions.LATVIA_MAP, GlobalCountryDivisions.LATVIA_MAP),
        CountryInfo("LT", "লিথুয়ানিয়া", "Lithuania", "🇱🇹", "+370", "€", "County", "City", "County", "City", GlobalCountryDivisions.LITHUANIA_MAP, GlobalCountryDivisions.LITHUANIA_MAP),
        CountryInfo("LU", "লুক্সেমবার্গ", "Luxembourg", "🇱🇺", "+352", "€", "Canton", "City", "Canton", "City", GlobalCountryDivisions.LUXEMBOURG_MAP, GlobalCountryDivisions.LUXEMBOURG_MAP),
        CountryInfo("IS", "আইসল্যান্ড", "Iceland", "🇮🇸", "+354", "ISK", "Region", "City", "Region", "City", GlobalCountryDivisions.ICELAND_MAP, GlobalCountryDivisions.ICELAND_MAP),
        CountryInfo("GE", "জর্জিয়া", "Georgia", "🇬🇪", "+995", "GEL", "Region", "City", "Region", "City", GlobalCountryDivisions.GEORGIA_MAP, GlobalCountryDivisions.GEORGIA_MAP),
        CountryInfo("KZ", "কাজাখস্তান", "Kazakhstan", "🇰🇿", "+7", "KZT", "Region", "City", "Region", "City", GlobalCountryDivisions.KAZAKHSTAN_MAP, GlobalCountryDivisions.KAZAKHSTAN_MAP),
        CountryInfo("UZ", "উজবেকিস্তান", "Uzbekistan", "🇺🇿", "+998", "UZS", "Region", "City", "Region", "City", GlobalCountryDivisions.UZBEKISTAN_MAP, GlobalCountryDivisions.UZBEKISTAN_MAP),
        CountryInfo("IR", "ইরান", "Iran", "🇮🇷", "+98", "IRR", "Province", "City", "Province", "City", GlobalCountryDivisions.IRAN_MAP, GlobalCountryDivisions.IRAN_MAP),
        CountryInfo("KH", "কম্বোডিয়া", "Cambodia", "🇰🇭", "+855", "KHR", "Province", "City", "Province", "City", GlobalCountryDivisions.CAMBODIA_MAP, GlobalCountryDivisions.CAMBODIA_MAP),
        CountryInfo("MM", "মায়ানমার", "Myanmar", "🇲🇲", "+95", "MMK", "Region", "City", "Region", "City", GlobalCountryDivisions.MYANMAR_MAP, GlobalCountryDivisions.MYANMAR_MAP),
        CountryInfo("MN", "মঙ্গোলিয়া", "Mongolia", "🇲🇳", "+976", "MNT", "Province", "City", "Province", "City", GlobalCountryDivisions.MONGOLIA_MAP, GlobalCountryDivisions.MONGOLIA_MAP),
        CountryInfo("FJ", "ফিজি", "Fiji", "🇫🇯", "+679", "FJD", "Division", "City", "Division", "City", GlobalCountryDivisions.FIJI_MAP, GlobalCountryDivisions.FIJI_MAP)
    )

    val ALL_COUNTRIES: List<CountryInfo> = PRIMARY_COUNTRIES + ADDITIONAL_COUNTRIES

    fun getCountryByName(name: String): CountryInfo {
        return ALL_COUNTRIES.find { it.nameBn.equals(name, ignoreCase = true) || it.nameEn.equals(name, ignoreCase = true) }
            ?: PRIMARY_COUNTRIES.first()
    }

    fun getCountryByCode(code: String): CountryInfo {
        return ALL_COUNTRIES.find { it.code.equals(code, ignoreCase = true) }
            ?: PRIMARY_COUNTRIES.first()
    }

    data class ProfessionItem(
        val id: String,
        val nameBn: String,
        val nameEn: String
    )

    val ALL_PROFESSION_ITEMS = listOf(
        ProfessionItem("all", "সকল পেশা", "All Professions"),
        ProfessionItem("electrician", "ইলেকট্রিক এবং বিদ্যুৎ (ইলেকট্রিশিয়ান)", "Electrician & Electrical"),
        ProfessionItem("plumber", "প্লাম্বার ও স্যানিটারি মিস্ত্রি", "Plumber & Sanitary"),
        ProfessionItem("carpenter", "কাঠমিস্ত্রি ও ফার্নিচার কারিগর", "Carpenter & Furniture Maker"),
        ProfessionItem("mason", "রাজমিস্ত্রি ও নির্মাণ কর্মী", "Mason & Construction Worker"),
        ProfessionItem("painter", "রংমিস্ত্রি ও ওয়াল পেইন্টার", "Painter & Wall Finisher"),
        ProfessionItem("ac", "এসি টেকনিশিয়ান ও মেকানিক", "AC Technician & Mechanic"),
        ProfessionItem("fridge", "ফ্রিজ ও রেফ্রিজারেটর টেকনিশিয়ান", "Refrigerator Technician"),
        ProfessionItem("tv", "টিভি ও মনিটর টেকনিশিয়ান", "TV & Display Monitor Tech"),
        ProfessionItem("auto", "গাড়ি ও বাইক মেকানিক", "Automobile & Bike Mechanic"),
        ProfessionItem("welder", "ওয়েল্ডার ও গ্রিল মিস্ত্রি", "Welder & Metal Fabricator"),
        ProfessionItem("tiles", "টাইলস ও মার্বেল মিস্ত্রি", "Tiles & Marble Worker"),
        ProfessionItem("cleaning", "হোম ক্লিনার ও হাউসকিপিং", "Home Cleaning & Housekeeping"),
        ProfessionItem("pest", "পেস্ট কন্ট্রোল (পোকা দমন)", "Pest Control Specialist"),
        ProfessionItem("cctv", "সিসিটিভি ও সিকিউরিটি টেকনিশিয়ান", "CCTV & Security Tech"),
        ProfessionItem("ceiling", "সিলিং ও জিপসাম মিস্ত্রি", "Ceiling & Gypsum Specialist"),
        ProfessionItem("solar", "সোলার প্যানেল টেকনিশিয়ান", "Solar Panel Technician"),
        ProfessionItem("generator", "জেনারেটর সার্ভিসিং টেকনিশিয়ান", "Generator Mechanic"),
        ProfessionItem("motor", "পানির পাম্প ও মোটর মেকানিক", "Water Pump & Motor Mechanic"),
        ProfessionItem("thai_aluminum", "থাই অ্যালুমিনিয়াম ও গ্লাস মিস্ত্রি", "Thai Aluminum & Glass Fitter"),
        ProfessionItem("computer", "কম্পিউটার ও ল্যাপটপ সার্ভিসিং", "Computer & Laptop Tech"),
        ProfessionItem("mobile", "মোবাইল ফোন রিপেয়ার টেকনিশিয়ান", "Mobile Phone Repair Tech"),
        ProfessionItem("network", "ওয়াইফাই ও ইন্টারনেট নেটওয়ার্ক টেকনিশিয়ান", "WiFi & Network Tech"),
        ProfessionItem("washing_machine", "ওয়াশিং মেশিন মেকানিক", "Washing Machine Technician"),
        ProfessionItem("microwave", "মাইক্রোওয়েভ ওভেন টেকনিশিয়ান", "Microwave Oven Technician"),
        ProfessionItem("ips_ups", "আইপিএস ও ইউপিএস টেকনিশিয়ান", "IPS & UPS Technician"),
        ProfessionItem("gas_stove", "গ্যাস স্টোভ ও চুলা মেকানিক", "Gas Stove & Burner Mechanic"),
        ProfessionItem("water_purifier", "ওয়াটার ফিল্টার টেকনিশিয়ান", "Water Filter Purifier Tech"),
        ProfessionItem("locksmith", "তালা-চাবি মেকার (লকস্মিথ)", "Locksmith & Key Maker"),
        ProfessionItem("glass_mirror", "কাঁচ ও গ্লাস কাটিং মিস্ত্রি", "Glass & Mirror Worker"),
        ProfessionItem("waterproofing", "ছাদ ও দেয়াল ওয়াটারপ্রুফিং", "Roof & Wall Waterproofing"),
        ProfessionItem("sofa_carpet", "সোফা ও কার্পেট ক্লিনার", "Sofa & Carpet Cleaner"),
        ProfessionItem("curtain_blinds", "পর্দা ও ব্লাইন্ড ফিটিংস", "Curtain & Blinds Fitter"),
        ProfessionItem("driver", "ড্রাইভার (ব্যক্তিগত ও বাণিজ্যিক)", "Personal & Commercial Driver"),
        ProfessionItem("lift", "লিফট ও এলিভেটর টেকনিশিয়ান", "Lift & Elevator Technician"),
        ProfessionItem("sound_system", "সাউন্ড সিস্টেম ও ডিজে টেকনিশিয়ান", "Sound System & DJ Tech"),
        ProfessionItem("lighting_deco", "লাইটিং ও স্টেজ ডেকোরেশন", "Lighting & Event Decorator"),
        ProfessionItem("furniture_polish", "ফার্নিচার পলিশ ও লেকার কারিগর", "Furniture Polish Specialist"),
        ProfessionItem("cook_chef", "বাবুর্চি ও রান্নার কারিগর", "Cook & Chef"),
        ProfessionItem("home_tutor", "হোম টিউটর / গৃহশিক্ষক", "Home Tutor & Teacher"),
        ProfessionItem("packers_movers", "বাসা বদল ও প্যাকার্স মুভার্স", "Packers & Movers Service"),
        ProfessionItem("metal_fabricator", "স্টিল ও মেটাল শিট ফেব্রিকেটর", "Steel & Metal Fabricator"),
        ProfessionItem("ebike_mechanic", "ইলেকট্রিক বাইক ও স্কুটার মেকানিক", "E-Bike & Scooter Mechanic"),
        ProfessionItem("security_guard", "সিকিউরিটি গার্ড ও নিরাপত্তা কর্মী", "Security Guard"),
        ProfessionItem("gardener", "মালি ও ছাদবাগান পরিচর্যাকারী", "Gardener & Landscaping"),
        ProfessionItem("tailor", "দর্জি ও টেইলারিং কারিগর", "Tailor & Clothing Fitter"),
        ProfessionItem("industrial_electric", "ইন্ডাস্ট্রিয়াল ইলেকট্রিশিয়ান", "Industrial Electrician"),
        ProfessionItem("heavy_welder", "কারখানা পাইপ ও হেভি ওয়েল্ডার", "Industrial Pipe Welder"),
        ProfessionItem("dent_paint", "গাড়ির ডেন্টিং ও পেইন্টিং মাস্টার", "Car Denting & Spray Painting"),
        ProfessionItem("drain_cleaning", "সেপটিক ট্যাংক ও ড্রেন ক্লিনিং", "Septic Tank & Drain Cleaner"),
        ProfessionItem("wallpaper_fitter", "ওয়ালপেপার ও থ্রিডি প্যানেল মিস্ত্রি", "Wallpaper & 3D Wall Panel Fitter"),
        ProfessionItem("signboard_neon", "সাইনবোর্ড ও এলইডি নিয়ন মেকার", "Signboard & Neon Light Maker"),
        ProfessionItem("geyser_mechanic", "গিজার ও ওয়াটার হিটার মেকানিক", "Geyser & Water Heater Tech"),
        ProfessionItem("truck_mechanic", "বাস ও ট্রাক হেভি মেকানিক", "Heavy Truck & Bus Mechanic"),
        ProfessionItem("crane_operator", "ক্রেন ও ফর্কলিফট অপারেটর", "Crane & Forklift Operator"),
        ProfessionItem("caregiver", "কেয়ারগিভার ও রোগী পরিচর্যাকারী", "Caregiver & Patient Attendant"),
        ProfessionItem("appliance_general", "সাধারণ গৃহস্থালি সরঞ্জাম মেরামত", "General Home Appliance Repair")
    )

    val PROFESSIONS = ALL_PROFESSION_ITEMS.map { it.nameBn }

    fun getProfessionsList(isBn: Boolean): List<String> {
        return ALL_PROFESSION_ITEMS.map { if (isBn) it.nameBn else it.nameEn }
    }

    fun getProfessionDisplayName(raw: String, isBn: Boolean): String {
        val trimmed = raw.trim()
        if (trimmed.isEmpty() || trimmed == "সকল পেশা" || trimmed == "All Professions") {
            return if (isBn) "সকল পেশা" else "All Professions"
        }
        val found = ALL_PROFESSION_ITEMS.find {
            it.nameBn.equals(trimmed, ignoreCase = true) ||
            it.nameEn.equals(trimmed, ignoreCase = true) ||
            it.id.equals(trimmed, ignoreCase = true) ||
            (it.id == "electrician" && (
                trimmed.contains("ইলেকট্রিক") ||
                trimmed.contains("বিদ্যুৎ") ||
                trimmed.contains("বৈদ্যুতিক") ||
                trimmed.contains("ইলেকট্রিশিয়ান") ||
                trimmed.contains("electric", ignoreCase = true)
            )) ||
            (it.id == "plumber" && (
                trimmed.contains("প্লাম্বার") ||
                trimmed.contains("স্যানিটারি") ||
                trimmed.contains("plumb", ignoreCase = true)
            )) ||
            (it.id == "ac" && (
                trimmed.contains("এসি") ||
                trimmed.contains("ac", ignoreCase = true)
            )) ||
            trimmed.contains(it.nameBn, ignoreCase = true) ||
            it.nameBn.contains(trimmed, ignoreCase = true) ||
            trimmed.contains(it.nameEn, ignoreCase = true) ||
            it.nameEn.contains(trimmed, ignoreCase = true)
        }
        return if (found != null) {
            if (isBn) found.nameBn else found.nameEn
        } else {
            raw
        }
    }

    fun matchesProfession(mistriProfession: String, filterProfession: String): Boolean {
        val f = filterProfession.trim()
        val m = mistriProfession.trim()
        if (f.isEmpty() || f == "সকল পেশা" || f == "All Professions") {
            return true
        }
        val found = ALL_PROFESSION_ITEMS.find {
            it.nameBn.equals(f, ignoreCase = true) ||
            it.nameEn.equals(f, ignoreCase = true) ||
            it.id.equals(f, ignoreCase = true) ||
            (it.id == "electrician" && (
                f.contains("ইলেকট্রিক") ||
                f.contains("বিদ্যুৎ") ||
                f.contains("বৈদ্যুতিক") ||
                f.contains("ইলেকট্রিশিয়ান") ||
                f.contains("electric", ignoreCase = true)
            ))
        }
        return if (found != null) {
            m.contains(found.nameBn, ignoreCase = true) ||
            m.contains(found.nameEn, ignoreCase = true) ||
            (found.id == "electrician" && (
                m.contains("ইলেকট্রিক") ||
                m.contains("বিদ্যুৎ") ||
                m.contains("বৈদ্যুতিক") ||
                m.contains("ইলেকট্রিশিয়ান") ||
                m.contains("electric", ignoreCase = true)
            )) ||
            (found.id == "plumber" && (
                m.contains("প্লাম্বার") ||
                m.contains("স্যানিটারি") ||
                m.contains("plumb", ignoreCase = true)
            )) ||
            (found.id == "ac" && (
                m.contains("এসি") ||
                m.contains("ac", ignoreCase = true)
            ))
        } else {
            m.contains(f, ignoreCase = true) || f.contains(m, ignoreCase = true)
        }
    }

    fun getUpazilasForDistrict(district: String, isBangladesh: Boolean = true): List<String> {
        val clean = district.trim()
        if (isBangladesh) {
            val match = BD_UPAZILAS_BN.entries.find { it.key.equals(clean, ignoreCase = true) || clean.contains(it.key, ignoreCase = true) }?.value
            if (match != null) return match
            val enMatch = BD_UPAZILAS_EN.entries.find { it.key.equals(clean, ignoreCase = true) || clean.contains(it.key, ignoreCase = true) }?.value
            if (enMatch != null) return enMatch
            return listOf("${clean} সদর", "পৌরসভা", "উপজেলা কেন্দ্র")
        } else {
            val intl = INTERNATIONAL_AREAS.entries.find { it.key.equals(clean, ignoreCase = true) || clean.contains(it.key, ignoreCase = true) }?.value
            if (intl != null) return intl
            val enMatch = BD_UPAZILAS_EN.entries.find { it.key.equals(clean, ignoreCase = true) || clean.contains(it.key, ignoreCase = true) }?.value
            if (enMatch != null) return enMatch
            return listOf("${clean} Central", "${clean} Downtown", "${clean} North", "${clean} South")
        }
    }

    fun getUpazilasForDistrict(district: String): List<String> = getUpazilasForDistrict(district, isBangladesh = true)
}
