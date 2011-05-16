package jp.co.dosanko.model.auto;


import jp.co.dosanko.csvutils.CSVAnnotation;
import org.apache.cayenne.CayenneDataObject;

/**
 * Class _Meibo was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Meibo extends CayenneDataObject {

    @CSVAnnotation(name="分類",priority=0)
    public static final String BUNRUI_PROPERTY = "bunrui";
    
    @CSVAnnotation(name="キャンパス属性",priority=1)
    public static final String CAMPUS_PROPERTY = "campus";

    @CSVAnnotation(name="局名",priority=2)
    public static final String KYOKU_PROPERTY = "kyoku";

    @CSVAnnotation(name="部・校室名",priority=3)
    public static final String BUKOUSHITSU_PROPERTY = "bukoushitsu";
    
    @CSVAnnotation(name="所属課（室）名",priority=4)
    public static final String SYOZOKUKA_PROPERTY = "syozokuka";

    @CSVAnnotation(name="分野・グループ名",priority=5)
    public static final String BGROUP_PROPERTY = "bgroup";

    
    @CSVAnnotation(name="専攻名",priority=6)
    public static final String SENKOU_PROPERTY = "senkou";

    @CSVAnnotation(name="分野名",priority=7)
    public static final String BUNYA_PROPERTY = "bunya";

    @CSVAnnotation(name="科目名",priority=8)
    public static final String KAMOKU_PROPERTY = "kamoku";

    @CSVAnnotation(name="教育講座名",priority=9)
    public static final String KYOUIKUKOUZA_PROPERTY = "kyouikukouza";

    @CSVAnnotation(name="役職",priority=10)
    public static final String YAKUSHOKU_PROPERTY = "yakushoku";

    @CSVAnnotation(name="名前",priority=11)
    public static final String NAME_PROPERTY = "name";

    @CSVAnnotation(name="なまえ",priority=12)
    public static final String HNAME_PROPERTY = "hname";

    @CSVAnnotation(name="ダイヤルイン",priority=13)
    public static final String TEL1_PROPERTY = "tel1";

    @CSVAnnotation(name="電話番号",priority=14)
    public static final String TEL2_PROPERTY = "tel2";

    @CSVAnnotation(name="FAX番号",priority=15)
    public static final String FAX_PROPERTY = "fax";
        
    
    @CSVAnnotation(name="メールアドレス",priority=16)
    public static final String MAIL_PROPERTY = "mail";
     
    
    public static final String JUNJYO_PROPERTY = "junjyo";
    

    public static final String ID_PK_COLUMN = "ID";

    
    public void setJunjyo(String junjyo){
        writeProperty("junjyo",junjyo);
    } 
    
    public String getJunjyo(){
        return (String)readProperty("junjyo");
    }
    
        public void setBunrui(String bunrui){
        writeProperty("bunrui",bunrui);
    } 
    
    public String getBunrui(){
        return (String)readProperty("bunrui");
    }
            
    public void setBukoushitsu(String bukoushitsu) {
        writeProperty("bukoushitsu", bukoushitsu);
    }
    public String getBukoushitsu() {
        return (String)readProperty("bukoushitsu");
    }

    public void setBunya(String bunya) {
        writeProperty("bunya", bunya);
    }
    public String getBunya() {
        return (String)readProperty("bunya");
    }

    public void setCampus(String campus) {
        writeProperty("campus", campus);
    }
    public String getCampus() {
        return (String)readProperty("campus");
    }

    public void setFax(String fax) {
        writeProperty("fax", fax);
    }
    public String getFax() {
        return (String)readProperty("fax");
    }

    public void setBgroup(String bgroup) {
        writeProperty("bgroup", bgroup);
    }
    public String getBgroup() {
        return (String)readProperty("bgroup");
    }

    public void setHname(String hname) {
        writeProperty("hname", hname);
    }
    public String getHname() {
        return (String)readProperty("hname");
    }

    public void setKamoku(String kamoku) {
        writeProperty("kamoku", kamoku);
    }
    public String getKamoku() {
        return (String)readProperty("kamoku");
    }

    public void setKyoku(String kyoku) {
        writeProperty("kyoku", kyoku);
    }
    public String getKyoku() {
        return (String)readProperty("kyoku");
    }

    public void setKyouikukouza(String kyouikukouza) {
        writeProperty("kyouikukouza", kyouikukouza);
    }
    public String getKyouikukouza() {
        return (String)readProperty("kyouikukouza");
    }

    public void setMail(String mail) {
        writeProperty("mail", mail);
    }
    public String getMail() {
        return (String)readProperty("mail");
    }

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
    }

    public void setSenkou(String senkou) {
        writeProperty("senkou", senkou);
    }
    public String getSenkou() {
        return (String)readProperty("senkou");
    }

    public void setSyozokuka(String syozokuka) {
        writeProperty("syozokuka", syozokuka);
    }
    public String getSyozokuka() {
        return (String)readProperty("syozokuka");
    }

    public void setTel1(String tel1) {
        writeProperty("tel1", tel1);
    }
    public String getTel1() {
        return (String)readProperty("tel1");
    }

    public void setTel2(String tel2) {
        writeProperty("tel2", tel2);
    }
    public String getTel2() {
        return (String)readProperty("tel2");
    }

    public void setYakushoku(String yakushoku) {
        writeProperty("yakushoku", yakushoku);
    }
    public String getYakushoku() {
        return (String)readProperty("yakushoku");
    }

}