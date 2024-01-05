package srb.akikrasic.sertifikat

import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.X500NameBuilder
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.FileInputStream
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

object certIKljuc {
    lateinit var cert: X509Certificate
    lateinit var kljuc: PrivateKey

    init {

        val cik = ucitavanjeCa()
        cert = cik.first
        kljuc = cik.second

    }

}
val sifra = "novaSifra".toCharArray()
fun ucitavanjeCa(): Pair<X509Certificate, PrivateKey>{
    val ks = KeyStore.getInstance("PKCS12", "BCFIPS")
    val f = FileInputStream("/home/aki/aCertCa.p12")

    ks.load(f,sifra)
    val cert = ks.getCertificate("k") as X509Certificate
    val kljuc = ks.getKey("k", sifra) as PrivateKey


    return Pair (cert, kljuc)

}
fun imeIzgradjivac(zemlja:String, lokacija:String, cn:String,org:String, orgJed:String  ):X500Name{
    return  X500NameBuilder(BCStyle.INSTANCE)
        .addRDN(BCStyle.L, lokacija)
        .addRDN(BCStyle.CN, cn)
        .addRDN(BCStyle.C, zemlja)
        .addRDN(BCStyle.O, org)
        .addRDN(BCStyle.OU, orgJed).build()

}
val provider = "BCFIPS"
fun generisanjeSertifikata(dnsName:String):KeyStore{
    val tip = "RSA"// "RSA"
    val algoritamPotpis ="SHA256withRSA" //menjano oktobar 2023
    val kpg = KeyPairGenerator.getInstance(tip,provider )
    kpg.initialize(2048)//
    val kp = kpg.generateKeyPair()
    val ser = BigInteger.valueOf(System.currentTimeMillis())
    val sad = Instant.now()
    val kraj =sad.plus(10*365, ChronoUnit.DAYS)
    val ime = imeIzgradjivac("RS","село Градиште Пирот",dnsName, "Програмирање и пољорпивреда", "Програмирање")
    val prosirivac = JcaX509ExtensionUtils()
    val izgradjivac =JcaX509v3CertificateBuilder(JcaX509CertificateHolder(certIKljuc.cert).subject,ser, Date.from(sad), Date.from(kraj), ime, kp.public)

    izgradjivac.addExtension(Extension.basicConstraints, true, BasicConstraints(false))
    izgradjivac.addExtension(Extension.subjectAlternativeName, false,
    GeneralNames(GeneralName(GeneralName.dNSName, dnsName)))

    izgradjivac.addExtension(Extension.subjectKeyIdentifier, false,prosirivac.createSubjectKeyIdentifier(certIKljuc.cert.publicKey) )

    izgradjivac.addExtension(Extension.authorityKeyIdentifier,false,prosirivac.createAuthorityKeyIdentifier(certIKljuc.cert))
    //izgradjivac.addExtension(Extension.keyUsage,false, KeyUsage(KeyUsage.keyAgreement ))
    izgradjivac.addExtension(Extension.keyUsage, true,  KeyUsage(KeyUsage.digitalSignature or KeyUsage.keyEncipherment or KeyUsage.dataEncipherment
    or KeyUsage.keyAgreement or KeyUsage.nonRepudiation or KeyUsage.cRLSign or KeyUsage.keyCertSign))

    val potpisivac = JcaContentSignerBuilder(algoritamPotpis).setProvider(provider).build(certIKljuc.kljuc)
    val drzac = izgradjivac.build(potpisivac)
    val cert = JcaX509CertificateConverter().getCertificate(drzac)

    val ks =   KeyStore.getInstance("PKCS12", "BCFIPS")
    ks.load(null, null)
    ks.setKeyEntry("k",kp.private,sifra, arrayOf<X509Certificate>(cert) )
    return ks
}

