import { FillCertListNumber_Async, getCertificates_Async, sign_Async } from './asyncCode';

var CodeApi = {
    GetSertList: function (cb) {
        var canAsync = !!cadesplugin.CreateObjectAsync;
        if (canAsync) {
            FillCertList_Async(cb)
        } else {
            FillCertList_NPAPI(cb)
        }
    },

    Common_SignCades: function (id, date, cb) {
        var canAsync = !!cadesplugin.CreateObjectAsync;
        if (canAsync) {
            return SignCades_Async(id, date, cb);
        } else {
            return SignCades_NPAPI(id, date, cb);
        }
    },

    FillCertInfo: function (id) {
        //var selectedCertID = id.selectedIndex;
        //var val = selectedCertID.options[selectedCertID].value.split(" ").reverse().join("").replace(/\s/g, "").toUpperCase();
        var canAsync = !!cadesplugin.CreateObjectAsync;
        if (canAsync) {
            sertInfo_as(id);
        } else {
            sertInfo_si(id);
        }
    },

    GetSertListNumber: function(cb, err){
        var canAsync = !!cadesplugin.CreateObjectAsync;
        if (canAsync) {
            return FillCertListNumber_Async();
        } else {
            return FillCertListNumber_NPAPI(cb, err)
        }
    },

    getCertificates: function(cb) {
        var canAsync = !!cadesplugin.CreateObjectAsync;
        if (canAsync) {
            return getCertificates_Async()
        } else {
            return getCertificates_NPAPI()
        }
    },

    sign: function (type, document, certificate, options) {
        var canAsync = !!cadesplugin.CreateObjectAsync;
        if (canAsync) {
            return sign_Async(type, document, certificate, options)
        } else {
            return sign_NPAPI(type, document, certificate, options)
        }
    },

    resetCadesPlugin: function () {
        cpcsp_chrome_nmcades && cpcsp_chrome_nmcades.ReleasePluginObjects();
    }
};

function FillCertListNumber_NPAPI(){
    return new Promise(function (resolve, reject) {
        try {
            var oStore = cadesplugin.CreateObject("CAdESCOM.Store");
            oStore.Open();
        }
        catch (ex) {
            reject("Ошибка при открытии хранилища: " + cadesplugin.getLastError(ex));
        }

        var certCnt;

        try {
            certCnt = oStore.Certificates.Count;
            if (certCnt !== 0) {
                return resolve();
            } else {
                return reject('Не найдено ни одного сертификата');
            }
            oStore.Close();
        }
        catch (ex) {
            oStore.Close();
            return;
        }
    });
}

function getCertificates_NPAPI(){
    return new Promise(function (resolve, reject) {
        try {
            var oStore = cadesplugin.CreateObject("CAdESCOM.Store");
            oStore.Open();
        }
        catch (ex) {
            reject("при открытии хранилища: " + cadesplugin.getLastError(ex));
            return;
        }

        var certCnt;
        var sertificates = [];
        var store = oStore.Certificates;

        try {
            certCnt = oStore.Certificates.Count;
            if (certCnt == 0)
                reject("Не найдено ни одного сертификата");
        }
        catch (ex) {
            oStore.Close();
            reject("Cannot find object or property. (0x80092004)")
        }

        for (var i = 1; i <= certCnt; i++) {
            var cert;
            try {
                cert = store.Item(i);
            }

            catch (ex) {
                reject("при перечислении сертификатов: " + cadesplugin.getLastError(ex));
                return;
            }

            var oOpt = {};
            var dateObj = new Date();
            try {
                if (dateObj < cert.ValidToDate && cert.HasPrivateKey() && cert.IsValid().Result) {
                    var certObj = new CertificateObj(cert);
                    oOpt.text = certObj.GetCertString();
                }
                else {
                    continue;
                }
            }
            catch (ex) {
                reject("при получении свойства SubjectName: " + cadesplugin.getLastError(ex));
            }
            try {
                oOpt.value = cert.Thumbprint;
            }
            catch (ex) {
                reject("при получении свойства Thumbprint: " + cadesplugin.getLastError(ex));
            }

            sertificates.push(oOpt);
        }
        try {
            oStore.Close();
            resolve(sertificates);
        } catch(ex) {
            reject("при закрытии хранилища: " + cadesplugin.getLastError(ex))
        }

    })
}

function sign_NPAPI(type, document, certificat, options) {
    return new Promise(function (resolve, reject) {
        let errormes;
        var thumbprint = certificat.split(" ").reverse().join("").replace(/\s/g, "").toUpperCase();
        try {
            var oStore = cadesplugin.CreateObject("CAdESCOM.Store");
            oStore.Open();
        } catch (err) {
            reject('Failed to create CAdESCOM.Store: ' + cadesplugin.getLastError(err));
            return;
        }

        var CAPICOM_CERTIFICATE_FIND_SHA1_HASH = 0;
        var oCerts = oStore.Certificates.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, thumbprint);

        if (oCerts.Count == 0) {
            reject("Certificate not found");
            return;
        }
        var certificate = oCerts.Item(1);

        var dataToSign = document;
        try {
            var oStore = cadesplugin.CreateObject("CAdESCOM.Store");
            oStore.Open();
        } catch (err) {
            reject('Ошибка при создании хранилища' + err.message);
            return;
        }

        var obj = {};
        obj.Name = certificate.SubjectName;
        obj.SignDate = new Date();
        obj.SerialNumber = certificate.SerialNumber;
        obj.ValidFrom = certificate.ValidFromDate;
        obj.ValidTo = certificate.ValidToDate;
        obj.Issuer = certificate.IssuerName;

        try {

            if (type === "XML") {
                try {
                    var oSigner = cadesplugin.CreateObject("CAdESCOM.CPSigner");
                } catch (err) {
                    errormes = 'при создании подписи';
                    throw errormes;
                }

                if (oSigner) {
                    oSigner.Certificate = certificate;
                }
                else {
                    errormes = "Failed to create CAdESCOM.CPSigner";
                    throw errormes;
                }

                var signMethod = "";
                var digestMethod = "";

                var pubKey = certificate.PublicKey();
                var algo = pubKey.Algorithm;
                var algoOid = algo.Value;
                if (algoOid == "1.2.643.7.1.1.1.1") {   // алгоритм подписи ГОСТ Р 34.10-2012 с ключом 256 бит
                    signMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102012-gostr34112012-256";
                    digestMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34112012-256";
                }
                else if (algoOid == "1.2.643.7.1.1.1.2") {   // алгоритм подписи ГОСТ Р 34.10-2012 с ключом 512 бит
                    signMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102012-gostr34112012-512";
                    digestMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34112012-512";
                }
                else if (algoOid == "1.2.643.2.2.19") {  // алгоритм ГОСТ Р 34.10-2001
                    signMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102001-gostr3411";
                    digestMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr3411";
                }
                else {
                    errormes = "Данная демо страница поддерживает XML подпись сертификатами с алгоритмом ГОСТ Р 34.10-2012, ГОСТ Р 34.10-2001";
                    throw errormes;
                }

                var CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED = 0;
                var CADESCOM_XML_SIGNATURE_TYPE_ENVELOPING = 1;

                try {
                    var oSignedXML = cadesplugin.CreateObject("CAdESCOM.SignedXML");
                } catch (err) {
                    reject("при попытке создания подписи" + err.message);
                    return;
                }

                oSignedXML.Content = dataToSign;
                if (options) {
                    if (options.TypeOfSign === 'ENVELOPING') {
                        oSignedXML.SignatureType = CADESCOM_XML_SIGNATURE_TYPE_ENVELOPING;
                    }
                    if (options.TypeOfSign === 'ENVELOPED') {
                        oSignedXML.SignatureType = CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED;
                    }
                } else {
                    oSignedXML.SignatureType = CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED;
                }
                //oSignedXML.SignatureType = CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED;

                oSignedXML.SignatureMethod = signMethod;
                oSignedXML.DigestMethod = digestMethod;

                var sSignedMessage = "";
                try {
                    var signature = oSignedXML.Sign(oSigner);
                }
                catch (err) {
                    errormes = "Не удалось создать подпись из-за ошибки: " + cadesplugin.getLastError(err);
                    throw errormes;
                }
            } else {
                var CADESCOM_CADES_BES = 1;

                var sHashValue = dataToSign;

                var CADESCOM_HASH_ALGORITHM_CP_GOST_3411 = 100;

                var oHashedData = InitializeHashedData(CADESCOM_HASH_ALGORITHM_CP_GOST_3411, sHashValue);

                var oSigner = cadesplugin.CreateObject("CAdESCOM.CPSigner");

                oSigner.Certificate = certificate;

                var oSignedData = cadesplugin.CreateObject("CAdESCOM.CadesSignedData");

                try {
                    var signature = oSignedData.SignHash(oHashedData, oSigner, CADESCOM_CADES_BES)
                } catch (err) {
                    errormes = "Не удалось создать подпись из-за ошибки " + err.message;
                    throw errormes;
                }
            }
            obj.Document = dataToSign;
            obj.SignedDocument = signature;

            oStore.Close();
            resolve(obj);
        }
        catch (err) {
            if (typeof err === 'string') {
                reject(err);
            } else {
                reject(err.message);
            }
            //document.getElementById("SignatureTxtBox").innerHTML = err;
        }
    })
}

export default CodeApi;