import java.util.Scanner;public class AltmanZScoreCalculator {    public static void main(String[] args) {        Scanner scanner = new Scanner(System.in);        // Firma tipi seçimini kullanıcıdan al        System.out.println("Firma Tipini Seçiniz:");        System.out.println("1- Halka Açık İmalat Sektörü Firmalar");        System.out.println("2- Özel Sektör İmalat Firmalar");        System.out.println("3- Özel Sektör Hizmet Firmalar");        System.out.print("Seçiminizi yapınız (1/2/3): ");        int secim = scanner.nextInt();        scanner.nextLine();  // Boş satırı okuma        // Kaç adet firma gireceğini kullanıcıdan al        System.out.print("Kaç adet firma gireceksiniz: ");        int firmaSayisi = scanner.nextInt();        scanner.nextLine();  // Boş satırı okuma        // Firma nesnelerini tutan bir dizi oluştur        Firma[] firmalar = new Firma[firmaSayisi];        // Kullanıcıdan firmaların bilgilerini alarak Firma nesnelerini oluştur        for (int i = 0; i < firmaSayisi; i++) {            firmalar[i] = firmaBilgileriniAl(i + 1);        }        // Tüm firmaların sonuçlarını birleştirip yazdır        StringBuilder sonuclar = new StringBuilder();        for (int i = 0; i < firmaSayisi; i++) {            sonuclar.append(hesaplaVeSonucuYazdir(firmalar[i]));            sonuclar.append(degerlendirFirmaDurumu(secim, firmalar[i]));            sonuclar.append("\n\n");        }        // Firmaların birleşmesi sonucu elde edilen X firması için değerleri hesapla        Firma birlesikFirma = birlesikFirmaHesapla(firmalar);        sonuclar.append("\nFirmaların birleşmesi sonucu elde edilen X firması için;\n");        sonuclar.append(hesaplaVeSonucuYazdir(birlesikFirma));        sonuclar.append(degerlendirFirmaDurumu(secim, birlesikFirma));        sonuclar.append("\n");        // Birleştirilen sonuçları ekrana yazdır        System.out.println("\n--- TÜM FİRMALARIN SONUÇLARI ---");        System.out.println(sonuclar);        // Giriş işlemlerini kapat        scanner.close();    }    // Diğer metotlar buraya gelecek...    // Firma tipine göre Altman Z Skor aralıklarına göre değerlendirme yapma metodu    public static String degerlendirFirmaDurumu(int secim, Firma firma) {        double zScore = hesaplaZScore(firma);        String firmaTipi = "";        String sonuc = "";        switch (secim) {            case 1:                firmaTipi = "Halka Açık İmalat Sektörü Firmalar";                if (zScore > 2.99) {                    sonuc = "Güvenli";                } else if (zScore >= 1.81) {                    sonuc = "Belirsiz";                } else {                    sonuc = "Sıkıntılı";                }                break;            case 2:                firmaTipi = "Özel Sektör İmalat Firmalar";                if (zScore > 2.90) {                    sonuc = "Güvenli";                } else if (zScore >= 1.23) {                    sonuc = "Belirsiz";                } else {                    sonuc = "Sıkıntılı";                }                break;            case 3:                firmaTipi = "Özel Sektör Hizmet Firmalar";                if (zScore > 2.60) {                    sonuc = "Güvenli";                } else if (zScore >= 1.1) {                    sonuc = "Belirsiz";                } else {                    sonuc = "Sıkıntılı";                }                break;            default:                sonuc = "Geçersiz seçim.";        }        // Nötrosofik Sayıları ve Normalleştirilmiş Değerleri Hesapla        double dogrulukDerecesi = 1 - (Math.abs(zScore - 2.99) / 2.99);        double belirsizlikDerecesi = 1 - Math.abs((zScore - 2.99) / 1.08);        double yanlislikDerecesi = 1 - dogrulukDerecesi - belirsizlikDerecesi;        return "DURUM: " + sonuc + "\n" +                "Doğruluk Derecesi: " + dogrulukDerecesi + "\n" +                "Belirsizlik Derecesi: " + belirsizlikDerecesi + "\n" +                "Yanlışlık Derecesi: " + yanlislikDerecesi + "\n";    }    // Firma nesnelerini birleştirerek yeni bir firma oluşturan metot    public static Firma birlesikFirmaHesapla(Firma[] firmalar) {        double toplamIsletmeSermayesi = 0;        double toplamToplamVarliklar = 0;        double toplamDagitilmayanKarlar = 0;        double toplamFaizVeVergiOncesiKar = 0;        double toplamOzkaynak = 0;        double toplamBorclar = 0;        double toplamSatislar = 0;        for (Firma firma : firmalar) {            toplamIsletmeSermayesi += firma.isletmeSermayesi;            toplamToplamVarliklar += firma.toplamVarliklar;            toplamDagitilmayanKarlar += firma.dagitilmayanKarlar;            toplamFaizVeVergiOncesiKar += firma.faizVeVergiOncesiKar;            toplamOzkaynak += firma.ozkaynak;            toplamBorclar += firma.borclar;            toplamSatislar += firma.satislar;        }        return new Firma("X",                toplamIsletmeSermayesi, toplamToplamVarliklar,                toplamDagitilmayanKarlar, toplamFaizVeVergiOncesiKar,                toplamOzkaynak, toplamBorclar, toplamSatislar);    }    // Altman Z-Skorunu hesaplayıp sonuçları yazdıran metot    public static String hesaplaVeSonucuYazdir(Firma firma) {        double x1 = firma.isletmeSermayesi / firma.toplamVarliklar;        double x2 = firma.dagitilmayanKarlar / firma.toplamVarliklar;        double x3 = firma.faizVeVergiOncesiKar / firma.toplamVarliklar;        double x4 = firma.ozkaynak / firma.borclar;        double zScore = 0.717 * x1 + 0.847 * x2 + 3.107 * x3 + 0.42 * x4;        StringBuilder result = new StringBuilder();        result.append("\n" + firma.isim + " için;\n");        result.append("İşletme Sermayesi = " + firma.isletmeSermayesi + "\n");        result.append("Toplam Varlıklar = " + firma.toplamVarliklar + "\n");        result.append("Dağıtılmayan Karlar = " + firma.dagitilmayanKarlar + "\n");        result.append("Faiz ve Vergi Öncesi Kar = " + firma.faizVeVergiOncesiKar + "\n");        result.append("Özkaynak = " + firma.ozkaynak + "\n");        result.append("Borçlar = " + firma.borclar + "\n");        result.append("Satışlar = " + firma.satislar + "\n");        result.append("\nVerilen bilgiler doğrultusunda " + firma.isim + " için;\n");        result.append("X1 = İşletme Sermayesi / Toplam Varlıklar = " + x1 + "\n");        result.append("X2 = Dağıtılmayan Karlar / Toplam Varlıklar = " + x2 + "\n");        result.append("X3 = Faiz ve Vergi Öncesi Kar / Toplam Varlıklar = " + x3 + "\n");        result.append("X4 = Özkaynak / Toplam Borç = " + x4 + "\n");        result.append("\nSonuç olarak, " + firma.isim + " için elde edilen Altman-Z Skoru;\n");        result.append("Z = 0.717 * X1 + 0.847 * X2 + 3.107 * X3 + 0.42 * X4 = " + zScore + "\n");        return result.toString();    }    // Firma nesnesini kullanıcının girdiği bilgilerle oluşturan metot    public static Firma firmaBilgileriniAl(int sira) {        Scanner scanner = new Scanner(System.in);        System.out.println("\n" + sira + ". Firma İsmi: ");        String isim = scanner.nextLine();        System.out.print("İşletme Sermayesi: ");        double isletmeSermayesi = scanner.nextDouble();        System.out.print("Toplam Varlıklar: ");        double toplamVarliklar = scanner.nextDouble();        System.out.print("Dagitilmayan Karlar: ");        double dagitilmayanKarlar = scanner.nextDouble();        System.out.print("Faiz ve Vergi Öncesi Kar: ");        double faizVeVergiOncesiKar = scanner.nextDouble();        System.out.print("Özkaynak: ");        double ozkaynak = scanner.nextDouble();        System.out.print("Borçlar: ");        double borclar = scanner.nextDouble();        System.out.print("Satışlar: ");        double satislar = scanner.nextDouble();        return new Firma(isim, isletmeSermayesi, toplamVarliklar,                dagitilmayanKarlar, faizVeVergiOncesiKar,                ozkaynak, borclar, satislar);    }    public static double hesaplaZScore(Firma firma) {        double x1 = firma.isletmeSermayesi / firma.toplamVarliklar;        double x2 = firma.dagitilmayanKarlar / firma.toplamVarliklar;        double x3 = firma.faizVeVergiOncesiKar / firma.toplamVarliklar;        double x4 = firma.ozkaynak / firma.borclar;        return 0.717 * x1 + 0.847 * x2 + 3.107 * x3 + 0.42 * x4;    }}