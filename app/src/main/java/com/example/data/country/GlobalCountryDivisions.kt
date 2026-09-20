package com.example.data.country

/**
 * Administrative divisions (States/Provinces/Regions) and major cities/districts
 * for global countries 19 to 100, ensuring every country in the app has complete
 * division and district selection data.
 */
object GlobalCountryDivisions {

    // 19. Portugal
    val PORTUGAL_MAP = mapOf(
        "Lisboa" to listOf("Lisbon", "Sintra", "Cascais", "Loures", "Amadora", "Oeiras"),
        "Porto" to listOf("Porto", "Vila Nova de Gaia", "Matosinhos", "Maia", "Gondomar"),
        "Braga" to listOf("Braga", "Guimarães", "Vila Nova de Famalicão", "Barcelos"),
        "Setúbal" to listOf("Setúbal", "Almada", "Seixal", "Barreiro", "Montijo"),
        "Faro (Algarve)" to listOf("Faro", "Portimão", "Loulé", "Lagos", "Albufeira"),
        "Coimbra" to listOf("Coimbra", "Figueira da Foz", "Cantanhede"),
        "Aveiro" to listOf("Aveiro", "Santa Maria da Feira", "Ílhavo", "Ovar"),
        "Leiria" to listOf("Leiria", "Caldas da Rainha", "Alcobaça")
    )

    // 20. Greece
    val GREECE_MAP = mapOf(
        "Attica" to listOf("Athens", "Piraeus", "Peristeri", "Kallithea", "Glyfada", "Marousi"),
        "Central Macedonia" to listOf("Thessaloniki", "Kalamaria", "Serres", "Katerini"),
        "Crete" to listOf("Heraklion", "Chania", "Rethymno", "Agios Nikolaos"),
        "Western Greece" to listOf("Patras", "Agrinio", "Aigio"),
        "Thessaly" to listOf("Larissa", "Volos", "Trikala", "Karditsa")
    )

    // 21. New Zealand
    val NEW_ZEALAND_MAP = mapOf(
        "Auckland" to listOf("Auckland Central", "North Shore", "Manukau", "Waitakere"),
        "Wellington" to listOf("Wellington City", "Lower Hutt", "Porirua", "Upper Hutt"),
        "Canterbury" to listOf("Christchurch", "Timaru", "Ashburton", "Rangiora"),
        "Waikato" to listOf("Hamilton", "Taupo", "Cambridge", "Te Awamutu"),
        "Bay of Plenty" to listOf("Tauranga", "Rotorua", "Whakatane"),
        "Otago" to listOf("Dunedin", "Queenstown", "Wanaka", "Oamaru")
    )

    // 22. Japan
    val JAPAN_MAP = mapOf(
        "Tokyo" to listOf("Shinjuku", "Shibuya", "Minato", "Chiyoda", "Chuo (Ginza)", "Setagaya", "Toshima (Ikebukuro)"),
        "Osaka" to listOf("Kita (Umeda)", "Chuo (Namba)", "Tennoji", "Sakai", "Higashiosaka"),
        "Kanagawa" to listOf("Yokohama", "Kawasaki", "Sagamihara", "Fujisawa", "Kamakura"),
        "Aichi" to listOf("Nagoya", "Toyota", "Okazaki", "Ichinomiya"),
        "Kyoto" to listOf("Kyoto Central", "Uji", "Kameoka", "Joyo"),
        "Fukuoka" to listOf("Fukuoka (Hakata)", "Fukuoka (Tenjin)", "Kitakyushu", "Kurume"),
        "Hokkaido" to listOf("Sapporo", "Asahikawa", "Hakodate", "Otaru"),
        "Saitama" to listOf("Saitama City", "Kawaguchi", "Kawagoe", "Tokorozawa"),
        "Chiba" to listOf("Chiba City", "Funabashi", "Matsudo", "Ichikawa"),
        "Hyogo" to listOf("Kobe", "Himeji", "Nishinomiya", "Amagasaki")
    )

    // 23. South Korea
    val SOUTH_KOREA_MAP = mapOf(
        "Seoul" to listOf("Gangnam", "Mapo", "Jongno", "Songpa", "Yongsan", "Jung-gu (Myeongdong)", "Yeongdeungpo"),
        "Gyeonggi-do" to listOf("Suwon", "Seongnam (Bundang)", "Goyang", "Yongin", "Bucheon", "Ansan", "Pyeongtaek"),
        "Busan" to listOf("Haeundae", "Busanjin (Seomyeon)", "Jung-gu (Nampo)", "Sasang", "Nam-gu"),
        "Incheon" to listOf("Bupyeong", "Songdo (Yeonsu)", "Namdong", "Seo-gu"),
        "Daegu" to listOf("Suseong", "Jung-gu", "Dalseo", "Buk-gu"),
        "Daejeon" to listOf("Yuseong", "Seo-gu", "Jung-gu"),
        "Gwangju" to listOf("Dong-gu", "Seo-gu", "Buk-gu", "Gwangsan")
    )

    // 24. Turkey
    val TURKEY_MAP = mapOf(
        "Istanbul" to listOf("Fatih", "Besiktas", "Sisli", "Kadikoy", "Uskudar", "Bakirkoy", "Beyoglu (Taksim)", "Esenyurt"),
        "Ankara" to listOf("Cankaya", "Kecioren", "Yenimahalle", "Mamak", "Etimesgut"),
        "Izmir" to listOf("Konak", "Karsiyaka", "Bornova", "Buca", "Bayrakli", "Cesme"),
        "Antalya" to listOf("Muratpasa", "Kepez", "Konyaalti", "Alanya", "Manavgat"),
        "Bursa" to listOf("Osmangazi", "Nilufer", "Yildirim", "Inegol"),
        "Adana" to listOf("Seyhan", "Yuregir", "Cukurova"),
        "Gaziantep" to listOf("Sahinbey", "Sehitkamil"),
        "Konya" to listOf("Selcuklu", "Meram", "Karatay")
    )

    // 25. Egypt
    val EGYPT_MAP = mapOf(
        "Cairo" to listOf("Nasr City", "New Cairo", "Heliopolis", "Maadi", "Zamalek", "Downtown Cairo", "Shubra"),
        "Giza" to listOf("Dokki", "Mohandessin", "6th of October City", "Sheikh Zayed", "Haram", "Faisal"),
        "Alexandria" to listOf("Montaza", "Mansheya", "Smouha", "Raml Station", "Sidi Gaber"),
        "Dakahlia" to listOf("Mansoura", "Mit Ghamr", "Talkha"),
        "Sharqia" to listOf("Zagazig", "10th of Ramadan City", "Bilbeis"),
        "Red Sea" to listOf("Hurghada", "El Gouna", "Marsa Alam"),
        "Luxor & Aswan" to listOf("Luxor City", "Aswan City", "Kom Ombo")
    )

    // 26. South Africa
    val SOUTH_AFRICA_MAP = mapOf(
        "Gauteng" to listOf("Johannesburg", "Pretoria", "Sandton", "Soweto", "Randburg", "Centurion"),
        "Western Cape" to listOf("Cape Town", "Stellenbosch", "Paarl", "George", "Somerset West"),
        "KwaZulu-Natal" to listOf("Durban", "Pietermaritzburg", "Umhlanga", "Pinetown", "Newcastle"),
        "Eastern Cape" to listOf("Gqeberha (Port Elizabeth)", "East London", "Mthatha"),
        "Free State" to listOf("Bloemfontein", "Welkom", "Sasolburg"),
        "Limpopo" to listOf("Polokwane", "Thohoyandou", "Tzaneen")
    )

    // 27. Maldives
    val MALDIVES_MAP = mapOf(
        "Kaafu Atoll (Malé)" to listOf("Malé City", "Hulhumalé", "Vilimalé", "Maafushi", "Thulusdhoo"),
        "Addu City" to listOf("Hithadhoo", "Maradhoo", "Feydhoo", "Hulhudhoo"),
        "Alif Alif Atoll" to listOf("Rasdhoo", "Thoddoo", "Ukulhas"),
        "Baa Atoll" to listOf("Eydhafushi", "Dharavandhoo", "Thulhaadhoo"),
        "Haa Alif Atoll" to listOf("Dhidhdhoo", "Ihavandhoo", "Kelaa")
    )

    // 28. Sri Lanka
    val SRI_LANKA_MAP = mapOf(
        "Western Province" to listOf("Colombo", "Gampaha", "Kalutara", "Dehiwala-Mount Lavinia", "Negombo", "Moratuwa"),
        "Central Province" to listOf("Kandy", "Matale", "Nuwara Eliya", "Gampola"),
        "Southern Province" to listOf("Galle", "Matara", "Hambantota"),
        "Northern Province" to listOf("Jaffna", "Kilinochchi", "Vavuniya", "Mannar"),
        "Eastern Province" to listOf("Trincomalee", "Batticaloa", "Ampara", "Kalmunai"),
        "North Western" to listOf("Kurunegala", "Puttalam", "Chilaw")
    )

    // 29. Nepal
    val NEPAL_MAP = mapOf(
        "Bagmati" to listOf("Kathmandu", "Lalitpur (Patan)", "Bhaktapur", "Hetauda", "Banepa"),
        "Gandaki" to listOf("Pokhara", "Gorkha", "Baglung", "Waling"),
        "Koshi" to listOf("Biratnagar", "Dharan", "Itahari", "Damak", "Bhadrapur"),
        "Lumbini" to listOf("Butwal", "Bhairahawa (Siddharthanagar)", "Nepalgunj", "Tulsipur"),
        "Madhesh" to listOf("Janakpur", "Birgunj", "Rajbiraj", "Lahan"),
        "Karnali" to listOf("Birendranagar (Surkhet)", "Jumla"),
        "Sudurpashchim" to listOf("Dhangadhi", "Bhimdatta (Mahendranagar)")
    )

    // 30. Pakistan
    val PAKISTAN_MAP = mapOf(
        "Punjab" to listOf("Lahore", "Faisalabad", "Rawalpindi", "Gujranwala", "Multan", "Sialkot", "Bahawalpur", "Sargodha"),
        "Sindh" to listOf("Karachi", "Hyderabad", "Sukkur", "Larkana", "Mirpur Khas"),
        "Islamabad Capital" to listOf("Islamabad Central", "Sector F", "Sector G", "Sector I", "DHA Islamabad", "Bahria Town"),
        "Khyber Pakhtunkhwa" to listOf("Peshawar", "Abbottabad", "Mardan", "Swat", "Nowshera"),
        "Balochistan" to listOf("Quetta", "Gwadar", "Turbat", "Khuzdar"),
        "Azad Kashmir" to listOf("Muzaffarabad", "Mirpur", "Rawalakot")
    )

    // 31. Thailand
    val THAILAND_MAP = mapOf(
        "Bangkok" to listOf("Sukhumvit", "Silom", "Siam", "Chatuchak", "Bang Rak", "Thonglor", "Phra Nakhon"),
        "Chiang Mai" to listOf("Mueang Chiang Mai", "Hang Dong", "Mae Rim", "San Sai"),
        "Phuket" to listOf("Patong", "Mueang Phuket", "Kathu", "Rawai", "Karon"),
        "Chonburi (Pattaya)" to listOf("Pattaya City", "Bang Lamung", "Si Racha", "Mueang Chonburi"),
        "Nonthaburi" to listOf("Mueang Nonthaburi", "Pak Kret", "Bang Bua Thong"),
        "Surat Thani (Koh Samui)" to listOf("Koh Samui", "Koh Phangan", "Mueang Surat Thani"),
        "Khon Kaen" to listOf("Mueang Khon Kaen", "Chum Phae")
    )

    // 32. Indonesia
    val INDONESIA_MAP = mapOf(
        "DKI Jakarta" to listOf("Central Jakarta", "South Jakarta", "West Jakarta", "North Jakarta", "East Jakarta"),
        "West Java" to listOf("Bandung", "Bekasi", "Depok", "Bogor", "Cimahi", "Cirebon"),
        "East Java" to listOf("Surabaya", "Malang", "Sidoarjo", "Jember", "Banyuwangi"),
        "Central Java" to listOf("Semarang", "Surakarta (Solo)", "Magelang", "Pekalongan"),
        "Bali" to listOf("Denpasar", "Badung (Kuta/Seminyak)", "Gianyar (Ubud)", "Tabanan"),
        "North Sumatra" to listOf("Medan", "Pematangsiantar", "Binjai", "Deli Serdang"),
        "Banten" to listOf("Tangerang", "South Tangerang (BSD)", "Serang", "Cilegon"),
        "DI Yogyakarta" to listOf("Yogyakarta City", "Sleman", "Bantul")
    )

    // 33. Vietnam
    val VIETNAM_MAP = mapOf(
        "Hanoi" to listOf("Hoan Kiem", "Ba Dinh", "Dong Da", "Cau Giay", "Tay Ho", "Hai Ba Trung"),
        "Ho Chi Minh City" to listOf("District 1", "District 2 (Thao Dien)", "District 7", "Binh Thanh", "Tan Binh", "Thu Duc"),
        "Da Nang" to listOf("Hai Chau", "Son Tra", "Ngu Hanh Son", "Thanh Khe"),
        "Hai Phong" to listOf("Hong Bang", "Ngo Quyen", "Le Chan"),
        "Can Tho" to listOf("Ninh Kieu", "Binh Thuy", "Cai Rang"),
        "Quang Ninh" to listOf("Ha Long City", "Cam Pha", "Uong Bi")
    )

    // 34. Philippines
    val PHILIPPINES_MAP = mapOf(
        "Metro Manila" to listOf("Manila", "Quezon City", "Makati", "Taguig (BGC)", "Pasig", "Mandaluyong", "Parañaque"),
        "Cebu" to listOf("Cebu City", "Mandaue", "Lapu-Lapu", "Talisay"),
        "Davao" to listOf("Davao City", "Tagum", "Panabo"),
        "Cavite" to listOf("Bacoor", "Dasmariñas", "Imus", "Tagaytay"),
        "Laguna" to listOf("Santa Rosa", "Calamba", "San Pedro", "Biñan"),
        "Pampanga" to listOf("Angeles City", "San Fernando", "Mabalacat"),
        "Iloilo" to listOf("Iloilo City", "Oton", "Pavia")
    )

    // 35. Brunei
    val BRUNEI_MAP = mapOf(
        "Brunei-Muara" to listOf("Bandar Seri Begawan", "Gadong", "Berakas", "Kianggeh", "Kilanas", "Sengkurong"),
        "Belait" to listOf("Kuala Belait", "Seria", "Sungai Liang"),
        "Tutong" to listOf("Pekan Tutong", "Keriam", "Kiudang"),
        "Temburong" to listOf("Pekan Bangar", "Batu Apoi", "Labu")
    )

    // 36. China
    val CHINA_MAP = mapOf(
        "Beijing" to listOf("Chaoyang", "Haidian", "Dongcheng", "Xicheng", "Fengtai", "Changping"),
        "Shanghai" to listOf("Pudong", "Huangpu", "Jing'an", "Xuhui", "Minhang", "Changning"),
        "Guangdong" to listOf("Guangzhou", "Shenzhen", "Dongguan", "Foshan", "Zhongshan", "Zhuhai"),
        "Zhejiang" to listOf("Hangzhou", "Ningbo", "Wenzhou", "Yiwu", "Shaoxing"),
        "Jiangsu" to listOf("Nanjing", "Suzhou", "Wuxi", "Changzhou", "Nantong"),
        "Sichuan" to listOf("Chengdu", "Mianyang", "Nanchong", "Yibin"),
        "Shandong" to listOf("Qingdao", "Jinan", "Yantai", "Weifang"),
        "Fujian" to listOf("Xiamen", "Fuzhou", "Quanzhou")
    )

    // 37. Russia
    val RUSSIA_MAP = mapOf(
        "Moscow" to listOf("Central Okrug", "Western Okrug", "Northern Okrug", "Southern Okrug", "South-Western Okrug"),
        "Saint Petersburg" to listOf("Central District", "Petrogradsky", "Vasileostrovsky", "Admiralteysky", "Moskovsky"),
        "Tatarstan" to listOf("Kazan", "Naberezhnye Chelny", "Nizhnekamsk", "Almetyevsk"),
        "Novosibirsk Oblast" to listOf("Novosibirsk", "Berdsk", "Iskitim"),
        "Sverdlovsk Oblast" to listOf("Yekaterinburg", "Nizhny Tagil", "Kamensk-Uralsky"),
        "Nizhny Novgorod" to listOf("Nizhny Novgorod", "Dzerzhinsk", "Arzamas"),
        "Krasnodar Krai" to listOf("Krasnodar", "Sochi", "Novorossiysk", "Anapa")
    )

    // 38. Brazil
    val BRAZIL_MAP = mapOf(
        "São Paulo" to listOf("São Paulo City", "Campinas", "Guarulhos", "São Bernardo do Campo", "Santos", "Ribeirão Preto"),
        "Rio de Janeiro" to listOf("Rio de Janeiro City", "Niterói", "Duque de Caxias", "Nova Iguaçu", "São Gonçalo"),
        "Minas Gerais" to listOf("Belo Horizonte", "Uberlândia", "Contagem", "Juiz de Fora"),
        "Bahia" to listOf("Salvador", "Feira de Santana", "Vitória da Conquista", "Camaçari"),
        "Paraná" to listOf("Curitiba", "Londrina", "Maringá", "Ponta Grossa", "Foz do Iguaçu"),
        "Rio Grande do Sul" to listOf("Porto Alegre", "Caxias do Sul", "Canoas", "Pelotas"),
        "Federal District" to listOf("Brasília", "Ceilândia", "Taguatinga", "Águas Claras")
    )

    // 39. Argentina
    val ARGENTINA_MAP = mapOf(
        "Buenos Aires" to listOf("Capital Federal", "La Plata", "Mar del Plata", "Quilmes", "Lanús", "San Isidro"),
        "Córdoba" to listOf("Córdoba City", "Villa Carlos Paz", "Río Cuarto", "Villa María"),
        "Santa Fe" to listOf("Rosario", "Santa Fe City", "Rafaela", "Venado Tuerto"),
        "Mendoza" to listOf("Mendoza City", "Godoy Cruz", "Guaymallén", "San Rafael"),
        "Tucumán" to listOf("San Miguel de Tucumán", "Yerba Buena", "Tafí Viejo"),
        "Salta" to listOf("Salta City", "San Ramón de la Nueva Orán")
    )

    // 40. Mexico
    val MEXICO_MAP = mapOf(
        "Mexico City (CDMX)" to listOf("Cuauhtémoc", "Miguel Hidalgo", "Benito Juárez", "Coyoacán", "Álvaro Obregón", "Tlalpan"),
        "Jalisco" to listOf("Guadalajara", "Zapopan", "Tlaquepaque", "Puerto Vallarta", "Tonalá"),
        "Nuevo León" to listOf("Monterrey", "San Pedro Garza García", "San Nicolás de los Garza", "Guadalupe", "Apodaca"),
        "Quintana Roo" to listOf("Cancún", "Playa del Carmen", "Cozumel", "Chetumal", "Tulum"),
        "Puebla" to listOf("Puebla City", "Tehuacán", "Cholula"),
        "Guanajuato" to listOf("León", "Irapuato", "Celaya", "Guanajuato City", "San Miguel de Allende"),
        "Yucatán" to listOf("Mérida", "Valladolid", "Progreso")
    )

    // 41. Sweden
    val SWEDEN_MAP = mapOf(
        "Stockholm" to listOf("Stockholm City", "Solna", "Nacka", "Huddinge", "Södertälje", "Täby"),
        "Västra Götaland" to listOf("Gothenburg", "Borås", "Mölndal", "Trollhättan", "Skövde"),
        "Skåne" to listOf("Malmö", "Helsingborg", "Lund", "Kristianstad", "Landskrona"),
        "Uppsala" to listOf("Uppsala City", "Enköping"),
        "Östergötland" to listOf("Linköping", "Norrköping", "Motala")
    )

    // 42. Norway
    val NORWAY_MAP = mapOf(
        "Oslo" to listOf("Sentrum", "Grünerløkka", "Frogner", "Majorstuen", "Gamle Oslo", "Nordstrand"),
        "Vestland" to listOf("Bergen", "Askøy", "Øygarden"),
        "Rogaland" to listOf("Stavanger", "Sandnes", "Haugesund"),
        "Trøndelag" to listOf("Trondheim", "Stjørdal", "Steinkjer"),
        "Viken" to listOf("Drammen", "Bærum", "Asker", "Fredrikstad", "Sarpsborg"),
        "Troms og Finnmark" to listOf("Tromsø", "Harstad", "Alta")
    )

    // 43. Denmark
    val DENMARK_MAP = mapOf(
        "Capital Region" to listOf("Copenhagen", "Frederiksberg", "Gentofte", "Gladsaxe", "Helsingør"),
        "Central Denmark" to listOf("Aarhus", "Randers", "Silkeborg", "Horsens", "Herning"),
        "North Denmark" to listOf("Aalborg", "Hjørring", "Frederikshavn"),
        "Region of Southern Denmark" to listOf("Odense", "Esbjerg", "Kolding", "Vejle"),
        "Zealand" to listOf("Roskilde", "Næstved", "Slagelse", "Køge")
    )

    // 44. Finland
    val FINLAND_MAP = mapOf(
        "Uusimaa" to listOf("Helsinki", "Espoo", "Vantaa", "Kauniainen", "Porvoo", "Järvenpää"),
        "Pirkanmaa" to listOf("Tampere", "Nokia", "Ylöjärvi", "Kangasala"),
        "Southwest Finland" to listOf("Turku", "Kaarina", "Raisio", "Salo"),
        "North Ostrobothnia" to listOf("Oulu", "Raahe", "Kuusamo"),
        "Central Finland" to listOf("Jyväskylä", "Jämsä", "Äänekoski")
    )

    // 45. Netherlands
    val NETHERLANDS_MAP = mapOf(
        "North Holland" to listOf("Amsterdam", "Haarlem", "Zaanstad", "Haarlemmermeer", "Alkmaar", "Hilversum"),
        "South Holland" to listOf("Rotterdam", "The Hague", "Leiden", "Dordrecht", "Zoetermeer", "Delft"),
        "Utrecht" to listOf("Utrecht City", "Amersfoort", "Veenendaal", "Zeist"),
        "North Brabant" to listOf("Eindhoven", "Tilburg", "Breda", "'s-Hertogenbosch", "Helmond"),
        "Gelderland" to listOf("Nijmegen", "Arnhem", "Apeldoorn", "Ede"),
        "Overijssel" to listOf("Enschede", "Zwolle", "Deventer", "Hengelo")
    )

    // 46. Belgium
    val BELGIUM_MAP = mapOf(
        "Brussels-Capital" to listOf("City of Brussels", "Ixelles", "Schaerbeek", "Anderlecht", "Saint-Gilles", "Uccle"),
        "Antwerp" to listOf("Antwerp City", "Mechelen", "Turnhout", "Lier"),
        "East Flanders" to listOf("Ghent", "Aalst", "Sint-Niklaas", "Dendermonde"),
        "West Flanders" to listOf("Bruges", "Kortrijk", "Ostend", "Roeselare"),
        "Flemish Brabant" to listOf("Leuven", "Vilvoorde", "Halle"),
        "Walloon Brabant" to listOf("Wavre", "Waterloo", "Ottignies-Louvain-la-Neuve"),
        "Liège" to listOf("Liège City", "Verviers", "Seraing")
    )

    // 47. Switzerland
    val SWITZERLAND_MAP = mapOf(
        "Zurich" to listOf("Zurich City", "Winterthur", "Uster", "Dübendorf"),
        "Geneva" to listOf("Geneva City", "Vernier", "Lancy", "Meyrin"),
        "Bern" to listOf("Bern City", "Biel/Bienne", "Thun", "Köniz"),
        "Vaud" to listOf("Lausanne", "Yverdon-les-Bains", "Montreux", "Nyon"),
        "Basel-Stadt" to listOf("Basel City", "Riehen", "Bettingen"),
        "Lucerne" to listOf("Lucerne City", "Emmen", "Kriens"),
        "Valais" to listOf("Sion", "Sierre", "Monthey", "Brig")
    )

    // 48. Austria
    val AUSTRIA_MAP = mapOf(
        "Vienna" to listOf("Innere Stadt", "Leopoldstadt", "Favoriten", "Donaustadt", "Floridsdorf", "Ottakring"),
        "Salzburg" to listOf("Salzburg City", "Hallein", "Saalfelden"),
        "Styria" to listOf("Graz", "Leoben", "Kapfenberg"),
        "Upper Austria" to listOf("Linz", "Wels", "Steyr"),
        "Tyrol" to listOf("Innsbruck", "Kufstein", "Telfs"),
        "Carinthia" to listOf("Klagenfurt", "Villach", "Wolfsberg")
    )

    // 49. Ireland
    val IRELAND_MAP = mapOf(
        "Dublin" to listOf("Dublin City", "Fingal (Swords)", "South Dublin (Tallaght)", "Dún Laoghaire-Rathdown"),
        "Cork" to listOf("Cork City", "Ballincollig", "Carrigaline", "Cobh", "Mallow"),
        "Galway" to listOf("Galway City", "Tuam", "Ballinasloe"),
        "Limerick" to listOf("Limerick City", "Castletroy", "Newcastle West"),
        "Waterford" to listOf("Waterford City", "Tramore", "Dungarvan"),
        "Kildare" to listOf("Naas", "Newbridge", "Celbridge", "Maynooth")
    )

    // 50. Poland
    val POLAND_MAP = mapOf(
        "Masovian" to listOf("Warsaw", "Radom", "Płock", "Siedlce", "Pruszków"),
        "Lesser Poland" to listOf("Kraków", "Tarnów", "Nowy Sącz", "Oświęcim"),
        "Lower Silesian" to listOf("Wrocław", "Wałbrzych", "Legnica", "Jelenia Góra"),
        "Greater Poland" to listOf("Poznań", "Kalisz", "Konin", "Piła", "Ostrów Wielkopolski"),
        "Pomeranian" to listOf("Gdańsk", "Gdynia", "Sopot", "Słupsk", "Tczew"),
        "Silesian" to listOf("Katowice", "Częstochowa", "Sosnowiec", "Gliwice", "Zabrze", "Bielsko-Biała")
    )

    // 51. Romania
    val ROMANIA_MAP = mapOf(
        "Bucharest" to listOf("Sector 1", "Sector 2", "Sector 3", "Sector 4", "Sector 5", "Sector 6"),
        "Cluj" to listOf("Cluj-Napoca", "Turda", "Dej", "Câmpia Turzii"),
        "Timiș" to listOf("Timișoara", "Lugoj", "Sânnicolau Mare"),
        "Iași" to listOf("Iași City", "Pașcani", "Hârlău"),
        "Constanța" to listOf("Constanța City", "Medgidia", "Mangalia"),
        "Brașov" to listOf("Brașov City", "Săcele", "Făgăraș"),
        "Prahova" to listOf("Ploiești", "Câmpina", "Băicoi")
    )

    // 52. Jordan
    val JORDAN_MAP = mapOf(
        "Amman" to listOf("Downtown Amman", "Abdoun", "Shmeisani", "Jabal Amman", "Sweifieh", "Jabal Al-Weibdeh", "Tlaa Al-Ali"),
        "Zarqa" to listOf("Zarqa City", "Rusaifa", "New Zarqa"),
        "Irbid" to listOf("Irbid City", "Ramtha", "Bani Kinana"),
        "Aqaba" to listOf("Aqaba Port", "Tala Bay", "Al-Rimal"),
        "Balqa" to listOf("Salt", "Fuheis", "Mahis", "Ain Al-Basha"),
        "Madaba" to listOf("Madaba City", "Dhiban")
    )

    // 53. Lebanon
    val LEBANON_MAP = mapOf(
        "Beirut" to listOf("Hamra", "Achrafieh", "Mar Mikhael", "Badaro", "Ras Beirut", "Verdun"),
        "Mount Lebanon" to listOf("Jounieh", "Baabda", "Metn (Jdeideh)", "Chouf", "Aley", "Byblos (Jbeil)"),
        "North Lebanon" to listOf("Tripoli", "Zgharta", "Batroun", "Bcharre"),
        "South Lebanon" to listOf("Sidon (Saida)", "Tyre (Sour)", "Jezzine"),
        "Bekaa" to listOf("Zahlé", "Baalbek", "West Bekaa")
    )

    // 54. Iraq
    val IRAQ_MAP = mapOf(
        "Baghdad" to listOf("Karkh", "Rusafa", "Al-Mansour", "Karrada", "Adhamiya", "Jadriya", "Zayouna"),
        "Basra" to listOf("Basra Center", "Zubair", "Abu Al-Khaseeb", "Qurna"),
        "Erbil (Kurdistan)" to listOf("Erbil Center", "Ankawa", "Ainkawa", "Salahaddin"),
        "Sulaymaniyah" to listOf("Sulaymaniyah Center", "Bakrajo", "Ranya"),
        "Najaf" to listOf("Najaf Center", "Kufa", "Manathera"),
        "Nineveh" to listOf("Mosul", "Tel Afar", "Hamdaniya"),
        "Karbala" to listOf("Karbala Center", "Ain Al-Tamur", "Hindiya")
    )

    // 55. Libya
    val LIBYA_MAP = mapOf(
        "Tripoli" to listOf("Tripoli Central", "Souq Al-Juma", "Janzour", "Tajoura", "Abu Salim"),
        "Benghazi" to listOf("Benghazi Central", "Al-Salmani", "Al-Sabri", "Garyounis"),
        "Misrata" to listOf("Misrata City", "Zliten", "Qasr Ahmad"),
        "Zawiya" to listOf("Zawiya City", "Sabratha", "Surman"),
        "Sabha" to listOf("Sabha City", "Murzuk", "Ghadduwah"),
        "Sirte" to listOf("Sirte City", "Harawa", "Bin Jawad")
    )

    // 56. Cyprus
    val CYPRUS_MAP = mapOf(
        "Nicosia" to listOf("Nicosia City", "Strovolos", "Lakatamia", "Aglantzia", "Engomi"),
        "Limassol" to listOf("Limassol City", "Germasogeia", "Agios Athanasios", "Ypsonas"),
        "Larnaca" to listOf("Larnaca City", "Aradippou", "Livadia", "Dromolaxia"),
        "Paphos" to listOf("Paphos City", "Geroskipou", "Peyia"),
        "Famagusta" to listOf("Paralimni", "Ayia Napa", "Deryneia")
    )

    // 57. Morocco
    val MOROCCO_MAP = mapOf(
        "Casablanca-Settat" to listOf("Casablanca", "Mohammedia", "El Jadida", "Settat", "Berrechid"),
        "Rabat-Salé-Kénitra" to listOf("Rabat", "Salé", "Kénitra", "Temara", "Sidi Slimane"),
        "Marrakech-Safi" to listOf("Marrakech", "Safi", "Essaouira", "Kelâat Es-Sraghna"),
        "Fès-Meknès" to listOf("Fes", "Meknes", "Taza", "Sefrou"),
        "Tangier-Tetouan" to listOf("Tangier", "Tetouan", "Larache", "Chefchaouen", "Al Hoceima")
    )

    // 58. Algeria
    val ALGERIA_MAP = mapOf(
        "Algiers" to listOf("Algiers Central", "Bab El Oued", "Kouba", "Hydra", "Bir Mourad Raïs", "Zeralda"),
        "Oran" to listOf("Oran City", "Es Senia", "Bir El Djir", "Arzew"),
        "Constantine" to listOf("Constantine City", "El Khroub", "Hamma Bouziane"),
        "Annaba" to listOf("Annaba City", "El Bouni", "Berrahal"),
        "Blida" to listOf("Blida City", "Boufarik", "Ouled Yaïch"),
        "Sétif" to listOf("Sétif City", "El Eulma", "Aïn Oulmene")
    )

    // 59. Tunisia
    val TUNISIA_MAP = mapOf(
        "Tunis" to listOf("Tunis Central", "La Marsa", "Carthage", "Sidi Bou Said", "Le Bardo"),
        "Sfax" to listOf("Sfax City", "Sakiet Ezzit", "Sakiet Eddaier"),
        "Sousse" to listOf("Sousse City", "Hammam Sousse", "Msaken"),
        "Ariana" to listOf("Ariana City", "Ettadhamen", "La Soukra", "Mnihla"),
        "Ben Arous" to listOf("Ben Arous City", "Radès", "Hammam Lif"),
        "Nabeul" to listOf("Nabeul City", "Hammamet", "Kelibia")
    )

    // 60. Kenya
    val KENYA_MAP = mapOf(
        "Nairobi" to listOf("Westlands", "Kilimani", "CBD", "Embakasi", "Karen", "Kasarani", "Lang'ata"),
        "Mombasa" to listOf("Mvita", "Nyali", "Likoni", "Changamwe", "Kisauni"),
        "Kisumu" to listOf("Kisumu Central", "Kisumu East", "Kisumu West"),
        "Nakuru" to listOf("Nakuru East", "Nakuru West", "Naivasha"),
        "Kiambu" to listOf("Thika", "Ruiru", "Kikuyu", "Kiambu Town"),
        "Uasin Gishu" to listOf("Eldoret", "Turbo", "Soy")
    )

    // 61. Nigeria
    val NIGERIA_MAP = mapOf(
        "Lagos" to listOf("Ikeja", "Victoria Island", "Lekki", "Surulere", "Yaba", "Ikoyi", "Alimosho"),
        "Abuja (FCT)" to listOf("Abuja Central", "Garki", "Wuse", "Maitama", "Asokoro", "Gwarinpa"),
        "Rivers" to listOf("Port Harcourt", "Obio-Akpor", "Bonny"),
        "Kano" to listOf("Kano Municipal", "Fagge", "Dala", "Nassarawa"),
        "Oyo" to listOf("Ibadan North", "Ibadan South-West", "Ogbomosho"),
        "Kaduna" to listOf("Kaduna North", "Kaduna South", "Zaria"),
        "Anambra" to listOf("Onitsha", "Awka", "Nnewi")
    )

    // 62. Ghana
    val GHANA_MAP = mapOf(
        "Greater Accra" to listOf("Accra Central", "Tema", "Madina", "Adenta", "Spintex", "East Legon"),
        "Ashanti" to listOf("Kumasi", "Obuasi", "Ejisu", "Asante Mampong"),
        "Western Region" to listOf("Sekondi-Takoradi", "Tarkwa", "Axim"),
        "Central Region" to listOf("Cape Coast", "Kasoa", "Winneba"),
        "Eastern Region" to listOf("Koforidua", "Nkawkaw", "Akim Oda"),
        "Northern Region" to listOf("Tamale", "Yendi", "Savelugu")
    )

    // 63. Chile
    val CHILE_MAP = mapOf(
        "Santiago Metropolitan" to listOf("Santiago Central", "Providencia", "Las Condes", "Maipú", "Puente Alto", "Vitacura"),
        "Valparaíso" to listOf("Valparaíso City", "Viña del Mar", "Quilpué", "Villa Alemana"),
        "Biobío" to listOf("Concepción", "Talcahuano", "San Pedro de la Paz", "Los Ángeles"),
        "Antofagasta" to listOf("Antofagasta City", "Calama"),
        "Araucanía" to listOf("Temuco", "Padre Las Casas", "Villarrica")
    )

    // 64. Colombia
    val COLOMBIA_MAP = mapOf(
        "Bogotá D.C." to listOf("Chapinero", "Usaquén", "Suba", "Teusaquillo", "Kennedy", "Engativá"),
        "Antioquia" to listOf("Medellín", "Envigado", "Bello", "Itagüí", "Rionegro"),
        "Valle del Cauca" to listOf("Cali", "Palmira", "Buenaventura", "Tuluá"),
        "Atlántico" to listOf("Barranquilla", "Soledad", "Malambo"),
        "Santander" to listOf("Bucaramanga", "Floridablanca", "Girón"),
        "Bolívar" to listOf("Cartagena", "Magangué", "Turbaco")
    )

    // 65. Peru
    val PERU_MAP = mapOf(
        "Lima" to listOf("Miraflores", "San Isidro", "Barranco", "Santiago de Surco", "San Borja", "Lima Centro"),
        "Arequipa" to listOf("Arequipa City", "Cayma", "Cerro Colorado", "Yanahuara"),
        "Cusco" to listOf("Cusco City", "Wanchaq", "San Sebastián"),
        "La Libertad" to listOf("Trujillo", "Víctor Larco", "Huanchaco"),
        "Piura" to listOf("Piura City", "Castilla", "Sullana"),
        "Lambayeque" to listOf("Chiclayo", "José Leonardo Ortiz", "La Victoria")
    )

    // 66. Ecuador
    val ECUADOR_MAP = mapOf(
        "Pichincha" to listOf("Quito", "Cumbayá", "Tumbaco", "Rumiñahui"),
        "Guayas" to listOf("Guayaquil", "Samborondón", "Durán", "Daule"),
        "Azuay" to listOf("Cuenca", "Gualaceo", "Paute"),
        "Manabí" to listOf("Manta", "Portoviejo", "Montecristi"),
        "El Oro" to listOf("Machala", "Pasaje", "Santa Rosa")
    )

    // 67. Venezuela
    val VENEZUELA_MAP = mapOf(
        "Capital District" to listOf("Libertador (Caracas)", "Catia", "El Valle"),
        "Miranda" to listOf("Chacao", "Baruta", "Sucre (Petare)", "El Hatillo", "Los Teques"),
        "Zulia" to listOf("Maracaibo", "San Francisco", "Cabimas"),
        "Carabobo" to listOf("Valencia", "Naguanagua", "San Diego", "Puerto Cabello"),
        "Aragua" to listOf("Maracay", "Turmero", "La Victoria"),
        "Lara" to listOf("Barquisimeto", "Cabudare", "Carora")
    )

    // 68. Bolivia
    val BOLIVIA_MAP = mapOf(
        "Santa Cruz" to listOf("Santa Cruz de la Sierra", "Montero", "Warnes"),
        "La Paz" to listOf("La Paz City", "El Alto", "Viacha"),
        "Cochabamba" to listOf("Cochabamba City", "Quillacollo", "Sacaba"),
        "Chuquisaca" to listOf("Sucre", "Monteagudo"),
        "Oruro" to listOf("Oruro City", "Huanuni")
    )

    // 69. Uruguay
    val URUGUAY_MAP = mapOf(
        "Montevideo" to listOf("Montevideo Centro", "Pocitos", "Carrasco", "Punta Carretas", "Ciudad Vieja"),
        "Canelones" to listOf("Ciudad de la Costa", "Las Piedras", "Pando"),
        "Maldonado" to listOf("Punta del Este", "Maldonado City", "San Carlos"),
        "Salto" to listOf("Salto City", "Daymán"),
        "Colonia" to listOf("Colonia del Sacramento", "Carmelo")
    )

    // 70. Paraguay
    val PARAGUAY_MAP = mapOf(
        "Asunción" to listOf("Asunción Central", "Villa Morra", "Carmelitas", "Sajonia"),
        "Central" to listOf("San Lorenzo", "Luque", "Lambaré", "Fernando de la Mora", "Capiatá"),
        "Alto Paraná" to listOf("Ciudad del Este", "Hernandarias", "Presidente Franco"),
        "Itapúa" to listOf("Encarnación", "Cambyretá", "Hohenau"),
        "Caaguazú" to listOf("Coronel Oviedo", "Caaguazú City")
    )

    // 71. Panama
    val PANAMA_MAP = mapOf(
        "Panamá" to listOf("Panama City", "San Francisco", "Bella Vista", "Costa del Este", "San Miguelito"),
        "Colón" to listOf("Colón City", "Cristóbal", "Cativá"),
        "Chiriquí" to listOf("David", "Boquete", "Bugaba"),
        "Panamá Oeste" to listOf("La Chorrera", "Arraiján", "Capira"),
        "Coclé" to listOf("Penonomé", "Antón", "Aguadulce")
    )

    // 72. Costa Rica
    val COSTA_RICA_MAP = mapOf(
        "San José" to listOf("San José Central", "Escazú", "Santa Ana", "Desamparados", "Montes de Oca"),
        "Alajuela" to listOf("Alajuela City", "San Ramón", "Grecia"),
        "Heredia" to listOf("Heredia City", "Belén", "Santo Domingo"),
        "Guanacaste" to listOf("Liberia", "Nicoya", "Santa Cruz", "Tamarindo"),
        "Cartago" to listOf("Cartago City", "La Unión", "Paraíso"),
        "Puntarenas" to listOf("Puntarenas City", "Jacó", "Quepos (Manuel Antonio)")
    )

    // 73. Guatemala
    val GUATEMALA_MAP = mapOf(
        "Guatemala" to listOf("Guatemala City (Zone 10/14)", "Mixco", "Villa Nueva", "Santa Catarina Pinula"),
        "Quetzaltenango" to listOf("Quetzaltenango (Xela)", "Salcajá", "Coatepeque"),
        "Sacatepéquez" to listOf("Antigua Guatemala", "Ciudad Vieja", "Jocotenango"),
        "Escuintla" to listOf("Escuintla City", "Santa Lucía Cotzumalguapa"),
        "Alta Verapaz" to listOf("Cobán", "San Pedro Carchá")
    )

    // 74. Cuba
    val CUBA_MAP = mapOf(
        "La Habana" to listOf("Old Havana (Habana Vieja)", "Vedado (Plaza)", "Miramar (Playa)", "Centro Habana"),
        "Santiago de Cuba" to listOf("Santiago de Cuba City", "Palma Soriano", "Contramaestre"),
        "Camagüey" to listOf("Camagüey City", "Florida", "Nuevitas"),
        "Holguín" to listOf("Holguín City", "Moa", "Banes"),
        "Matanzas" to listOf("Matanzas City", "Varadero", "Cárdenas"),
        "Villa Clara" to listOf("Santa Clara", "Sagua la Grande", "Placetas")
    )

    // 75. Jamaica
    val JAMAICA_MAP = mapOf(
        "Kingston" to listOf("Downtown Kingston", "New Kingston", "Port Royal"),
        "Saint Andrew" to listOf("Half Way Tree", "Liguanea", "Constant Spring", "Stony Hill"),
        "Saint James" to listOf("Montego Bay", "Rose Hall", "Anchovy"),
        "Saint Ann" to listOf("Ocho Rios", "St. Ann's Bay", "Runaway Bay"),
        "Saint Catherine" to listOf("Spanish Town", "Portmore", "Old Harbour")
    )

    // 76. Bahamas
    val BAHAMAS_MAP = mapOf(
        "New Providence" to listOf("Nassau Downtown", "Paradise Island", "Cable Beach", "Lyford Cay"),
        "Grand Bahama" to listOf("Freeport", "Lucaya", "West End"),
        "Abaco Islands" to listOf("Marsh Harbour", "Hope Town", "Treasure Cay"),
        "Eleuthera" to listOf("Governor's Harbour", "Harbour Island", "Rock Sound")
    )

    // 77. Barbados
    val BARBADOS_MAP = mapOf(
        "Saint Michael" to listOf("Bridgetown", "Belleville", "Black Rock"),
        "Christ Church" to listOf("Oistins", "Hastings", "Worthing", "Rockley"),
        "Saint James" to listOf("Holetown", "Sunset Crest", "Paynes Bay"),
        "Saint George" to listOf("The Valley", "Gun Hill", "Boarded Hall")
    )

    // 78. Czech Republic
    val CZECH_MAP = mapOf(
        "Prague" to listOf("Prague 1 (Staré Město)", "Prague 2 (Vinohrady)", "Prague 4", "Prague 5 (Smíchov)", "Prague 7 (Holešovice)"),
        "South Moravian" to listOf("Brno", "Znojmo", "Hodonín", "Břeclav"),
        "Moravian-Silesian" to listOf("Ostrava", "Havířov", "Karviná", "Opava", "Frýdek-Místek"),
        "Plzeň" to listOf("Plzeň City", "Klatovy", "Rokycany"),
        "Central Bohemian" to listOf("Kladno", "Mladá Boleslav", "Příbram", "Kolín")
    )

    // 79. Hungary
    val HUNGARY_MAP = mapOf(
        "Budapest" to listOf("District V (Belváros)", "District VII (Erzsébetváros)", "District XI (Újbuda)", "District XIII (Angyalföld)"),
        "Pest" to listOf("Érd", "Dunakeszi", "Szigetszentmiklós", "Gödöllő", "Cegléd"),
        "Hajdú-Bihar" to listOf("Debrecen", "Hajdúszoboszló", "Hajdúböszörmény"),
        "Csongrád-Csanád" to listOf("Szeged", "Hódmezővásárhely", "Makó"),
        "Győr-Moson-Sopron" to listOf("Győr", "Sopron", "Mosonmagyaróvár")
    )

    // 80. Ukraine
    val UKRAINE_MAP = mapOf(
        "Kyiv" to listOf("Shevchenkivskyi", "Pecherskyi", "Obolonskyi", "Podilskyi", "Darnytskyi", "Solomianskyi"),
        "Lviv" to listOf("Halych", "Sykhiv", "Lychakiv", "Frankivskyi", "Shevchenkivskyi"),
        "Odesa" to listOf("Prymorskyi", "Suvorovskyi", "Kyivskyi", "Malynovskyi"),
        "Kharkiv" to listOf("Shevchenkivskyi", "Kyivskyi", "Saltivka", "Kholodnohirskyi"),
        "Dnipro" to listOf("Sobornyi", "Shevchenkivskyi", "Tsentralnyi", "Amur-Nyzhnodniprovskyi"),
        "Zaporizhzhia" to listOf("Oleksandrivskyi", "Voznesenivskyi", "Dniprovskyi")
    )

    // 81. Belarus
    val BELARUS_MAP = mapOf(
        "Minsk" to listOf("Tsentralny", "Frunzenski", "Maskouski", "Savetski", "Pershamayski"),
        "Brest" to listOf("Leninski", "Maskouski", "Baranavichy", "Pinsk"),
        "Gomel" to listOf("Tsentralny", "Savetski", "Chyhunachny", "Mazyr"),
        "Grodno" to listOf("Leninski", "Kastrychnitski", "Lida"),
        "Mogilev" to listOf("Leninski", "Kastrychnitski", "Babruysk"),
        "Vitebsk" to listOf("Kastrychnitski", "Chyhunachny", "Orsha", "Navapolatsk")
    )

    // 82. Bulgaria
    val BULGARIA_MAP = mapOf(
        "Sofia City" to listOf("Sofia Central", "Lozenets", "Mladost", "Lyulin", "Triaditsa"),
        "Plovdiv" to listOf("Central Plovdiv", "Trakia", "Severen", "Yuzhen"),
        "Varna" to listOf("Odessos", "Primorski", "Mladost", "Asparuhovo"),
        "Burgas" to listOf("Burgas Center", "Lazur", "Izgrev", "Meden Rudnik"),
        "Ruse" to listOf("Ruse Center", "Vazrazhdane", "Charodeyka"),
        "Stara Zagora" to listOf("Stara Zagora Center", "Zheleznik", "Kazandzhiev")
    )

    // 83. Serbia
    val SERBIA_MAP = mapOf(
        "Belgrade" to listOf("Stari Grad", "Vračar", "Novi Beograd", "Zemun", "Savski Venac", "Palilula"),
        "South Bačka" to listOf("Novi Sad", "Petrovaradin", "Sremska Kamenica", "Vrbas"),
        "Nišava" to listOf("Medijana (Niš)", "Palilula (Niš)", "Pantelej", "Aleksinac"),
        "Šumadija" to listOf("Kragujevac", "Aranđelovac", "Topola"),
        "Rasina" to listOf("Kruševac", "Trstenik", "Aleksandrovac")
    )

    // 84. Croatia
    val CROATIA_MAP = mapOf(
        "Zagreb" to listOf("Donji Grad", "Gornji Grad - Medveščak", "Novi Zagreb", "Trešnjevka", "Maksimir"),
        "Split-Dalmatia" to listOf("Split", "Kaštela", "Solin", "Trogir", "Makarska"),
        "Primorje-Gorski Kotar" to listOf("Rijeka", "Opatija", "Crikvenica", "Krk"),
        "Osijek-Baranja" to listOf("Osijek", "Đakovo", "Beli Manastir", "Našice"),
        "Istria" to listOf("Pula", "Poreč", "Rovinj", "Umag")
    )

    // 85. Slovakia
    val SLOVAKIA_MAP = mapOf(
        "Bratislava" to listOf("Staré Mesto", "Ružinov", "Petržalka", "Nové Mesto", "Dúbravka"),
        "Košice" to listOf("Staré Mesto", "Západ", "Dargovských hrdinov", "Sever"),
        "Prešov" to listOf("Prešov City", "Poprad", "Bardejov", "Humenné"),
        "Žilina" to listOf("Žilina City", "Martin", "Čadca", "Kysucké Nové Mesto"),
        "Banská Bystrica" to listOf("Banská Bystrica City", "Zvolen", "Lučenec")
    )

    // 86. Slovenia
    val SLOVENIA_MAP = mapOf(
        "Central Slovenia" to listOf("Ljubljana", "Domžale", "Kamnik", "Vrhnika", "Grosuplje"),
        "Drava" to listOf("Maribor", "Ptuj", "Slovenska Bistrica"),
        "Savinja" to listOf("Celje", "Velenje", "Žalec"),
        "Coastal-Karst" to listOf("Koper", "Izola", "Piran", "Sežana"),
        "Gorenjska" to listOf("Kranj", "Jesenice", "Radovljica", "Bled")
    )

    // 87. Bosnia and Herzegovina
    val BOSNIA_MAP = mapOf(
        "Sarajevo Canton" to listOf("Centar", "Stari Grad", "Novo Sarajevo", "Novi Grad", "Ilidža"),
        "Tuzla Canton" to listOf("Tuzla", "Živinice", "Gračanica", "Lukavac"),
        "Zenica-Doboj" to listOf("Zenica", "Visoko", "Tešanj", "Zavidovići"),
        "Herzegovina-Neretva" to listOf("Mostar", "Konjic", "Čapljina", "Jablanica"),
        "Banja Luka Region" to listOf("Banja Luka", "Prijedor", "Gradiška", "Laktaši")
    )

    // 88. Estonia
    val ESTONIA_MAP = mapOf(
        "Harju" to listOf("Tallinn (Kesklinn)", "Tallinn (Mustamäe)", "Tallinn (Lasnamäe)", "Keila", "Saue"),
        "Tartu" to listOf("Tartu City", "Elva", "Kambja"),
        "Ida-Viru" to listOf("Narva", "Kohtla-Järve", "Jõhvi", "Sillamäe"),
        "Pärnu" to listOf("Pärnu City", "Sindi", "Tori"),
        "Lääne-Viru" to listOf("Rakvere", "Tapa", "Kadrina")
    )

    // 89. Latvia
    val LATVIA_MAP = mapOf(
        "Riga" to listOf("Central District", "Vidzeme Suburb", "Kurzeme District", "Zemgale Suburb", "Latgale Suburb"),
        "Daugavpils" to listOf("Daugavpils City", "Jaunbūve", "Griva"),
        "Liepāja" to listOf("Liepāja City", "Karosta", "Jaunliepāja"),
        "Jelgava" to listOf("Jelgava City", "Ozolnieki"),
        "Jūrmala" to listOf("Majori", "Dzintari", "Bulduri", "Kauguri")
    )

    // 90. Lithuania
    val LITHUANIA_MAP = mapOf(
        "Vilnius" to listOf("Old Town (Senamiestis)", "Naujamiestis", "Antakalnis", "Žirmūnai", "Šnipiškės"),
        "Kaunas" to listOf("Centras", "Žaliakalnis", "Dainava", "Šilainiai", "Aleksotas"),
        "Klaipėda" to listOf("Klaipėda Center", "Baltija", "Žvejybos uostas"),
        "Šiauliai" to listOf("Šiauliai Center", "Dainiai", "Gytariai"),
        "Panevėžys" to listOf("Panevėžys Center", "Klaipėdos", "Rožynas")
    )

    // 91. Luxembourg
    val LUXEMBOURG_MAP = mapOf(
        "Luxembourg Canton" to listOf("Luxembourg City", "Bonnevoie", "Kirchberg", "Gasperich", "Limpertsberg", "Hesperange"),
        "Esch-sur-Alzette" to listOf("Esch-sur-Alzette City", "Differdange", "Dudelange", "Pétange", "Sanem"),
        "Diekirch" to listOf("Diekirch City", "Ettelbruck", "Erpeldange"),
        "Grevenmacher" to listOf("Grevenmacher City", "Mondorf-les-Bains", "Remich")
    )

    // 92. Iceland
    val ICELAND_MAP = mapOf(
        "Capital Region" to listOf("Reykjavík", "Kópavogur", "Hafnarfjörður", "Garðabær", "Mosfellsbær"),
        "Northeastern Region" to listOf("Akureyri", "Húsavík", "Dalvík"),
        "Southern Region" to listOf("Selfoss", "Vestmannaeyjar", "Hveragerði"),
        "Western Region" to listOf("Akranes", "Borgarnes", "Stykkishólmur")
    )

    // 93. Georgia
    val GEORGIA_MAP = mapOf(
        "Tbilisi" to listOf("Vake", "Saburtalo", "Mtatsminda", "Isani", "Samgori", "Didube", "Gldani"),
        "Adjara" to listOf("Batumi", "Kobuleti", "Khelvachauri"),
        "Imereti" to listOf("Kutaisi", "Samtredia", "Zestaponi"),
        "Kvemo Kartli" to listOf("Rustavi", "Marneuli", "Gardabani"),
        "Samegrelo" to listOf("Zugdidi", "Poti", "Senaki")
    )

    // 94. Kazakhstan
    val KAZAKHSTAN_MAP = mapOf(
        "Almaty" to listOf("Almaly", "Bostandyk", "Medeu", "Auezov", "Nauryzbay"),
        "Astana" to listOf("Yesil", "Almaty District", "Saryarka", "Baikonur", "Nura"),
        "Shymkent" to listOf("Al-Farabi", "Enbekshi", "Karatau", "Abay"),
        "Karaganda" to listOf("Kazybek Bi", "Oktyabrsky", "Temirtau"),
        "Aktobe" to listOf("Aktobe City", "Alga", "Khromtau"),
        "Atyrau" to listOf("Atyrau City", "Balykshi", "Kulsary")
    )

    // 95. Uzbekistan
    val UZBEKISTAN_MAP = mapOf(
        "Tashkent City" to listOf("Mirabad", "Yunusabad", "Chilanzar", "Yakkasaray", "Shaykhontohur", "Mirzo Ulugbek"),
        "Samarkand" to listOf("Samarkand City", "Katta-Kurgan", "Urgut"),
        "Bukhara" to listOf("Bukhara City", "Gijduvan", "Kagan"),
        "Fergana" to listOf("Fergana City", "Kokand", "Margilan"),
        "Namangan" to listOf("Namangan City", "Chust", "Kosonsoy"),
        "Andijan" to listOf("Andijan City", "Asaka", "Shahrikhan")
    )

    // 96. Iran
    val IRAN_MAP = mapOf(
        "Tehran" to listOf("Tehran Central", "Shemiranat (Tajrish)", "Ray", "Eslamshahr", "Shahriar"),
        "Razavi Khorasan" to listOf("Mashhad", "Nishapur", "Sabzevar", "Torbat-e Heydarieh"),
        "Isfahan" to listOf("Isfahan City", "Kashan", "Khomeyni Shahr", "Najafabad"),
        "Fars" to listOf("Shiraz", "Marvdasht", "Jahrom", "Fasa"),
        "East Azerbaijan" to listOf("Tabriz", "Maragheh", "Marand"),
        "Khuzestan" to listOf("Ahvaz", "Abadan", "Dezful", "Khorramshahr")
    )

    // 97. Cambodia
    val CAMBODIA_MAP = mapOf(
        "Phnom Penh" to listOf("Daun Penh", "Chamkar Mon", "Tuol Kouk", "Boeung Keng Kang", "Sen Sok", "Prampir Makara"),
        "Siem Reap" to listOf("Siem Reap City", "Puok", "Prasat Bakong"),
        "Battambang" to listOf("Battambang City", "Moung Ruessei", "Thma Koul"),
        "Preah Sihanouk" to listOf("Sihanoukville", "Prey Nob", "Koh Rong"),
        "Kandal" to listOf("Ta Khmau", "Kien Svay", "Sa'ang")
    )

    // 98. Myanmar
    val MYANMAR_MAP = mapOf(
        "Yangon" to listOf("Kyauktada", "Dagon", "Bahan", "Kamayut", "Hlaing", "Insein", "Tamwe"),
        "Mandalay" to listOf("Chanayethazan", "Mahaaungmye", "Aungmyethazan", "Chanmyathazi"),
        "Naypyidaw" to listOf("Zabuthiri", "Ottarathiri", "Dekkhinathiri", "Pobbathiri"),
        "Bago" to listOf("Bago City", "Pyay", "Taungoo"),
        "Shan State" to listOf("Taunggyi", "Lashio", "Muse", "Kalaw")
    )

    // 99. Mongolia
    val MONGOLIA_MAP = mapOf(
        "Ulaanbaatar" to listOf("Sükhbaatar", "Bayanzürkh", "Khan Uul", "Chingeltei", "Bayangol", "Songino Khairkhan"),
        "Darkhan-Uul" to listOf("Darkhan City", "Khongor", "Sharyngol"),
        "Orkhon" to listOf("Erdenet City", "Bayan-Öndör", "Jargalant"),
        "Selenge" to listOf("Sükhbaatar City", "Züünburen", "Tsagaanbaatar")
    )

    // 100. Fiji
    val FIJI_MAP = mapOf(
        "Central Division" to listOf("Suva", "Nausori", "Lami", "Navua"),
        "Western Division" to listOf("Nadi", "Lautoka", "Ba", "Sigatoka", "Rakiraki"),
        "Northern Division" to listOf("Labasa", "Savusavu", "Nabouwalu"),
        "Eastern Division" to listOf("Levuka", "Lomaiviti", "Kadavu")
    )
}
