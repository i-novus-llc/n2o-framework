var global_certificates = {};

function CertificateAdjuster() {
}

CertificateAdjuster.prototype.extract = function (from, what) {
    let certName = "";

    var begin = from.indexOf(what);

    if (begin >= 0) {
        var end = from.indexOf(', ', begin);
        certName = (end < 0) ? from.substr(begin) : from.substr(begin, end - begin);
    }

    return certName;
}

CertificateAdjuster.prototype.Print2Digit = function (digit) {
    return (digit < 10) ? "0" + digit : digit;
}

CertificateAdjuster.prototype.GetCertDate = function (paramDate) {
    var certDate = new Date(paramDate);
    return this.Print2Digit(certDate.getUTCDate()) + "." + this.Print2Digit(certDate.getMonth() + 1) + "." + certDate.getFullYear() + " " +
        this.Print2Digit(certDate.getUTCHours()) + ":" + this.Print2Digit(certDate.getUTCMinutes()) + ":" + this.Print2Digit(certDate.getUTCSeconds());
}

CertificateAdjuster.prototype.GetCertName = function (certSubjectName) {
    var snVal = this.extract(certSubjectName, 'SN=');
    var gVal = this.extract(certSubjectName, 'G=');
    if (snVal != "" && gVal != "")
        return snVal + "; " + gVal;
    else
        return this.extract(certSubjectName, 'CN=');
}

CertificateAdjuster.prototype.GetIssuer = function (certIssuerName) {
    return this.extract(certIssuerName, 'CN=');
}

CertificateAdjuster.prototype.GetCertInfoString = function (certSubjectName, certFromDate) {
    var snVal = this.extract(certSubjectName, 'SN=');
    var gVal = this.extract(certSubjectName, 'G=');
    var certName;
    if (snVal != "" && gVal != "")
        certName = snVal + "; " + gVal;
    else
        certName = this.extract(certSubjectName, 'CN=');
    return certName + "; Выдан: " + this.GetCertDate(certFromDate);
}

function CheckForPlugIn_Async() {
    function VersionCompare_Async(StringVersion, ObjectVersion) {
        if (typeof(ObjectVersion) == "string")
            return -1;
        var arr = StringVersion.split('.');
        var isActualVersion = true;

        cadesplugin.async_spawn(function * ()
            {
                if ((yield ObjectVersion.MajorVersion) == parseInt(arr[0])) {
                    if ((yield ObjectVersion.MinorVersion) == parseInt(arr[1])) {
                        if ((yield ObjectVersion.BuildVersion) == parseInt(arr[2])) {
                            isActualVersion = true;
                        }
                        else if ((yield ObjectVersion.BuildVersion) < parseInt(arr[2])) {
                            isActualVersion = false;
                        }
                    } else if ((yield ObjectVersion.MinorVersion) < parseInt(arr[1])) {
                        isActualVersion = false;
                    }
                } else if ((yield ObjectVersion.MajorVersion) < parseInt(arr[0])) {
                    isActualVersion = false;
                }

                if (!isActualVersion) {
                    document.getElementById('PluginEnabledImg').setAttribute("src", "Img/yellow_dot.png");
                    document.getElementById('PlugInEnabledTxt').innerHTML = "Плагин загружен, но есть более свежая версия.";
                }
                document.getElementById('PlugInVersionTxt').innerHTML = "Версия плагина: " + (yield CurrentPluginVersion.toString());
                var oAbout = yield cadesplugin.CreateObjectAsync("CAdESCOM.About");
                var ver = yield oAbout.CSPVersion("", 75);
                var ret = (yield ver.MajorVersion) + "." + (yield ver.MinorVersion) + "." + (yield ver.BuildVersion);
                document.getElementById('CSPVersionTxt').innerHTML = "Версия криптопровайдера: " + ret;

                try {
                    var sCSPName = yield oAbout.CSPName(75);
                    document.getElementById('CSPNameTxt').innerHTML = "Криптопровайдер: " + sCSPName;
                }
                catch (err) {
                }
                return;
            }
        )
        ;
    }

    function GetLatestVersion_Async(CurrentPluginVersion) {
        var xmlhttp = getXmlHttp();
        xmlhttp.open("GET", "/sites/default/files/products/cades/latest_2_0.txt", true);
        xmlhttp.onreadystatechange = function () {
            var PluginBaseVersion;
            if (xmlhttp.readyState == 4) {
                if (xmlhttp.status == 200) {
                    PluginBaseVersion = xmlhttp.responseText;
                    VersionCompare_Async(PluginBaseVersion, CurrentPluginVersion)
                }
            }
        }
        xmlhttp.send(null);
    }

    //document.getElementById('PluginEnabledImg').setAttribute("src", "Img/green_dot.png");
    //document.getElementById('PlugInEnabledTxt').innerHTML = "Плагин загружен.";
    var CurrentPluginVersion;
    cadesplugin.async_spawn(function * ()
        {
            var oAbout = yield cadesplugin.CreateObjectAsync("CAdESCOM.About");
            CurrentPluginVersion = yield oAbout.PluginVersion;
            GetLatestVersion_Async(CurrentPluginVersion);
            FillCertList_Async('CertListBox');
            // var txtDataToSign = "Hello World";
            // document.getElementById("DataToSignTxtBox").innerHTML = txtDataToSign;
            // document.getElementById("SignatureTxtBox").innerHTML = "";
        }
    )
    ; //cadesplugin.async_spawn
}

function TrustedUrls(url) {
    cadesplugin.async_spawn(function * (arg)
        {
            try {
                var PluginObject = yield cpcsp_chrome_nmcades.CreatePluginObject();
                var g_oCfg = yield PluginObject.CreateObjectAsync("CAdESCOM.PluginConfiguration");
            }
            catch (err) {
                // try/catch just works, rejected promises are thrown here
                alert("Exception: " + err.message);
            }

            g_oSites = yield g_oCfg.TrustedSites;
            yield g_oSites.Add(arg[0]);
            //var sitesCnt = yield g_oSites.Count;
        }
        ,
        url
    )
    ;
}

function sertInfo_as(id) {
    //if(!cb.$el.children().length) { return }
    //document.getElementById('cert_info').classList.add('loading')
    cadesplugin.async_spawn(function * (arg)
        {
            document.getElementById('cert_info').classList.add('loading');
            var e = document.getElementById(arg[0]);
            var selectedCertID = e.selectedIndex;
            var thumbprint = e.options[selectedCertID].value;
            var certificate = global_certificates[thumbprint];

            FillCertInfo_Async(certificate);
            document.getElementById('cert_info').classList.remove('loading')
            //var oCerts = yield all_certs.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, thumbprint);
        }
        ,
        id
    )
    ;
}

export function FillCertListNumber_Async() {
    return new Promise(function (resolve, reject) {
        cadesplugin.async_spawn(function * (arg) {

                var oStore = yield cadesplugin.CreateObjectAsync("CAdESCOM.Store");

                if (!oStore) {
                    reject("Ошибка при создании хранилища");
                    return;
                }

                try {
                    yield oStore.Open();
                }
                catch (ex) {
                    reject("Ошибка при открытии хранилища " + ex.message);
                    return;
                }
                var certCnt;
                var certs;

                try {
                    certs = yield oStore.Certificates;
                    certCnt = yield certs.Count;
                }
                catch (ex) {
                    reject('Ошибка при перечислении сертификатов ' + ex.message);
                    return;
                }

                if (certCnt !== 0) {
                    //cb.pluginError("Не найденно сертификатов")
                    return resolve();
                } else {
                    return reject('Не найдено ни одного сертификата');
                }
                yield oStore.Close();
            },
            this, resolve, reject
        );
    });
}

export function getCertificates_Async () {
    return new Promise(function (resolve, reject) {
        cadesplugin.async_spawn(function * (arg) {
            var oStore = yield cadesplugin.CreateObjectAsync("CAdESCOM.Store");
            if (!oStore) {
                reject("При создании хранилища");
                return;
            }

            try {
                yield oStore.Open();
            }
            catch (ex) {
                reject("При открытии хранилища " + ex.message);
                return;
            }

            var sertificates = [];
            var certCnt;
            var certs;

            try {
                certs = yield oStore.Certificates;
                certCnt = yield certs.Count;
            }
            catch (ex) {
                reject('Ошибка при перечислении сертификатов ' + ex.message);
                return;
            }

            if (certCnt == 0) {
                reject("Не найденно сертификатов");
                return;
            }

            for (var i = 1; i <= certCnt; i++) {
                var cert;
                try {
                    cert = yield certs.Item(i);
                }
                catch (ex) {
                    reject("Ошибка при перечислении сертификатов " + ex.message);
                    return;
                }

                var proc = Math.round((certCnt / 100) * i);

                var oOpt = {};
                var dateObj = new Date();
                try {
                    var ValidToDate = new Date((yield cert.ValidToDate));
                    var ValidFromDate = new Date((yield cert.ValidFromDate));
                    var Validator = yield cert.IsValid();
                    var IsValid = yield Validator.Result;
                    var SubjectName = yield cert.SubjectName;
                    if (dateObj < ValidToDate && (yield cert.HasPrivateKey()) && IsValid) {
                        oOpt.text = new CertificateAdjuster().GetCertInfoString(SubjectName, ValidFromDate);
                    }
                    else {
                        continue;
                    }
                }
                catch (ex) {
                    reject("Ошибка при получении информации сертификата " + ex.message);
                }
                try {
                    oOpt.value = yield cert.Thumbprint;
                }
                catch (ex) {
                    reject("Ошибка при получении свойства Thumbprint");
                }
                sertificates.push(oOpt);
            }
            yield oStore.Close();

            resolve(sertificates);
        }, this, resolve, reject);
    });
}

export function sign_Async(type, document, certificat, options) {
    return new Promise(function(resolve, reject){
        cadesplugin.async_spawn(function * (arg)
        {
            var thumbprint = certificat.split(" ").reverse().join("").replace(/\s/g, "").toUpperCase();
            try {
                var oStore = yield cadesplugin.CreateObjectAsync("CAdESCOM.Store");
                yield oStore.Open();
            } catch (err) {
                rejejct("Не удаётся создать хранилище");
                return;
            }

            var CAPICOM_CERTIFICATE_FIND_SHA1_HASH = 0;
            var all_certs = yield oStore.Certificates;
            var oCerts = yield all_certs.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, thumbprint);

            if ((yield oCerts.Count) == 0) {
                reject("Не найдено сертификатов");
                return;
            }
            var certificate = yield oCerts.Item(1);
            var obj = {};
            obj.Name = yield certificate.SubjectName;

            var Adjust = new CertificateAdjuster();
            var ValidFrom = Adjust.GetCertDate(yield certificate.ValidFromDate);
            var ValidTo = Adjust.GetCertDate(yield certificate.ValidToDate);

            var time = new Date();
            obj.SignDate = time.getFullYear() + '.' + time.getMonth() + '.' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
            obj.SerialNumber = yield certificate.SerialNumber;
            obj.ValidFrom = ValidFrom;
            obj.ValidTo = ValidTo;
            obj.Issuer = yield certificate.IssuerName;

            var dataToSign = document;
            //var SignatureFieldTitle = document.getElementsByName("SignatureTitle");
            var Signature;
            try {
                //FillCertInfo_Async(certificate);
                var errormes = "";
                try {
                    var oSigner = yield cadesplugin.CreateObjectAsync("CAdESCOM.CPSigner");
                } catch (err) {
                    errormes = "Failed to create CAdESCOM.CPSigner: " + err.message;
                    throw errormes;
                }
                if (oSigner) {
                    yield oSigner.propset_Certificate(certificate);
                }
                else {
                    errormes = "Failed to create CAdESCOM.CPSigner";
                    throw errormes;
                }
                if (type === "XML") {
                    var oSignedXML = yield cadesplugin.CreateObjectAsync("CAdESCOM.SignedXML");

                    var XmlDsigGost3410Url = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102001-gostr3411";
                    var XmlDsigGost3411Url = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr3411";
                    var CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED = 0;
                    var CADESCOM_XML_SIGNATURE_TYPE_ENVELOPING = 1;

                    if (dataToSign) {
                        yield oSignedXML.propset_Content(dataToSign);
                        if (options) {
                            if (options.TypeOfSign === 'ENVELOPING') {
                                yield oSignedXML.propset_SignatureType(CADESCOM_XML_SIGNATURE_TYPE_ENVELOPING);
                            }
                            if (options.TypeOfSign === 'ENVELOPED') {
                                yield oSignedXML.propset_SignatureType(CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED);
                            }
                        } else {
                            yield oSignedXML.propset_SignatureType(CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED);
                        }
                        yield oSignedXML.propset_SignatureMethod(XmlDsigGost3410Url);
                        yield oSignedXML.propset_DigestMethod(XmlDsigGost3411Url);

                        try {
                            Signature = yield oSignedXML.Sign(oSigner);
                        }
                        catch (err) {
                            errormes = "Не удалось создать подпись из-за ошибки: " + err.message;

                            throw errormes;
                        }
                    }
                } else {
                    var sHashValue = dataToSign;

                    var oHashedData = yield cadesplugin.CreateObjectAsync("CAdESCOM.HashedData");

                    var signAlgo = cadesplugin.CADESCOM_HASH_ALGORITHM_CP_GOST_3411;
                    var pk = yield certificate.PublicKey();
                    var algo = yield pk.Algorithm;
                    var algoValue = yield algo.Value;
                    if (algoValue === '1.2.643.7.1.1.1.1') {
                        signAlgo = cadesplugin.CADESCOM_HASH_ALGORITHM_CP_GOST_3411_2012_256
                    }

                    yield oHashedData.Algorithm = signAlgo;

                    yield oHashedData.SetHashValue(sHashValue);

                    var oSignedData = yield cadesplugin.CreateObjectAsync("CAdESCOM.CadesSignedData");

                    var cadesProfile = resolveAdesProfile(options);

                    if ( options.TsaAddress ) {
                        yield oSigner.propset_TSAAddress(options.TsaAddress);
                    }

                    try {
                        Signature = yield oSignedData.SignHash(oHashedData, oSigner, cadesProfile)
                    } catch (err) {
                        errormes = "Не удалось создать подпись " + err.message;
                        throw errormes;
                    }
                }
                obj.Document = dataToSign;
                obj.SignedDocument = Signature;
                try {
                    yield oStore.Close();
                    resolve(obj);
                } catch (err) {
                    errormes = "Не удалось закрыть хранилище: " + err.message;
                    throw errormes;
                }
            }
            catch (err) {
                reject(err);
            }
        }, type, document, certificat, options, resolve, reject);
    })
}

function FillCertList_Async(cb) {
    cadesplugin.async_spawn(function * ()
        {
            try {
                var oStore = yield cadesplugin.CreateObjectAsync("CAdESCOM.Store");
            }
            catch (ex) {
                cb.pluginError("При открытии хранилища " + ex.message);
                return;
            }
            if (!oStore) {
                cb.pluginError("При создании хранилища");
                return;
            }

            try {
                yield oStore.Open();
            }
            catch (ex) {
                cb.pluginError("При открытии хранилища " + ex.message);
                return;
            }

            var sertificates = [];
            var certCnt;
            var certs;
            global_certificates = [];

            var lst = document.getElementById("CertListBox");

            try {
                certs = yield oStore.Certificates;
                certCnt = yield certs.Count;
            }
            catch (ex) {
                cb.pluginError('Ошибка при перечислении сертификатов ' + ex.message);
                return;
            }

            if (certCnt == 0) {
                cb.pluginError("Не найденно сертификатов");
                return;
            }

            /*var progress = 0;
             var iter = Math.round()(certCnt/100)
             var pros = 1;*/
            //_.throttle(cb.load(), 500);

            for (var i = 1; i <= certCnt; i++) {
                if (!cb.$el.children().length) {
                    break
                }
                var cert;
                try {
                    cert = yield certs.Item(i);
                }
                catch (ex) {
                    cb.pluginError("Ошибка при перечислении сертификатов " + ex.message);
                    cb.unloading();
                    return;
                }

                var proc = Math.round((certCnt / 100) * i)

                cb.throttle();

                var oOpt = document.createElement("OPTION");
                var dateObj = new Date();
                try {
                    var ValidToDate = new Date((yield cert.ValidToDate));
                    var ValidFromDate = new Date((yield cert.ValidFromDate));
                    var Validator = yield cert.IsValid();
                    var IsValid = yield Validator.Result;
                    var SubjectName = yield cert.SubjectName;
                    var HasPrivateKey = yield cert.HasPrivateKey();
                    if (dateObj < ValidToDate && HasPrivateKey && IsValid) {
                        oOpt.text = new CertificateAdjuster().GetCertInfoString(SubjectName, ValidFromDate);
                    }
                    else {
                        continue;
                    }
                }
                catch (ex) {
                    cb.pluginError("Ошибка при получении информации сертификата " + ex.message);
                }
                try {
                    var Thumbprint = yield cert.Thumbprint;
                    oOpt.value = Thumbprint;
                    global_certificates[Thumbprint] = cert;
                }
                catch (ex) {
                    cb.pluginError("Ошибка при получении свойства Thumbprint");
                }
                sertificates.push(oOpt);
            }
            yield oStore.Close();


            //В версии плагина 2.0.13292+ есть возможность получить сертификаты из
            //закрытых ключей и не установленных в хранилище
            try {
                yield oStore.Open(cadesplugin.CADESCOM_CONTAINER_STORE);
                certs = yield oStore.Certificates;
                certCnt = yield certs.Count;
                for (var i = 1; i <= certCnt; i++) {
                    var cert = yield certs.Item(i);
                    //Проверяем не добавляли ли мы такой сертификат уже?
                    var found = false;
                    for (var j = 0; j < sertificates.length; j++)
                    {
                        if ((yield sertificates[j].Thumbprint) === (yield cert.Thumbprint))
                        {
                            found = true;
                            break;
                        }
                    }
                    if(found)
                        continue;
                    var oOpt = document.createElement("OPTION");
                    var dateObj = new Date();
                    try {
                        var ValidToDate = new Date((yield cert.ValidToDate));
                        var ValidFromDate = new Date((yield cert.ValidFromDate));
                        var Validator = yield cert.IsValid();
                        var IsValid = yield Validator.Result;
                        var SubjectName = yield cert.SubjectName;
                        var HasPrivateKey = yield cert.HasPrivateKey();
                        if (dateObj < ValidToDate && HasPrivateKey && IsValid) {
                            oOpt.text = new CertificateAdjuster().GetCertInfoString(SubjectName, ValidFromDate);
                        }
                        else {
                            continue;
                        }
                    }
                    catch (ex) {
                        cb.pluginError("Ошибка при получении информации сертификата " + ex.message);
                    }
                    try {
                        var Thumbprint = yield cert.Thumbprint;
                        oOpt.value = Thumbprint;
                        global_certificates[Thumbprint] = cert;
                    }
                    catch (ex) {
                        cb.pluginError("Ошибка при получении свойства Thumbprint");
                    }
                    sertificates.push(oOpt);
                }
                yield oStore.Close();

            }
            catch (ex) {
            }

            cb.unloading();
            sertificates.forEach(function (item) {
                lst.options.add(item)
            });
            sertInfo_as('CertListBox');

            if (sertificates.length === 1) {
                lst.style.display = 'none';
            }
        }
    )
    ;//cadesplugin.async_spawn
}

function SignCades_Async(certListBoxId, XML, cb) {
    cadesplugin.async_spawn(function * (arg)
        {
            document.getElementById('cert_info').classList.add('loading')
            var e = document.getElementById(arg[0]);
            var selectedCertID = e.selectedIndex;

            var thumbprint = e.options[selectedCertID].value;
            try {
                var oStore = yield cadesplugin.CreateObjectAsync("CAdESCOM.Store");
                yield oStore.Open();
            } catch (err) {
                document.getElementById('cert_info').classList.remove('loading')
                cb.pluginError("Не удаётся создать хранилище");
                return;
            }

            var certificate = global_certificates[thumbprint];
            if (!certificate) {
                document.getElementById('cert_info').classList.remove('loading')
                cb.pluginError("Не найдено сертификатов");
                return;
            }
            var obj = {};
            obj.Name = yield certificate.SubjectName;
            var pk = yield certificate.PrivateKey;
            pk.propset_CachePin(cb.needCachePin);

            var Adjust = new CertificateAdjuster();
            var ValidFrom = Adjust.GetCertDate(yield certificate.ValidFromDate)
            var ValidTo = Adjust.GetCertDate(yield certificate.ValidToDate)

            var time = new Date()
            obj.SignDate = time.getFullYear() + '.' + time.getMonth() + '.' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
            obj.SerialNumber = yield certificate.SerialNumber;
            obj.ValidFrom = ValidFrom;
            obj.ValidTo = ValidTo;
            obj.Issuer = yield certificate.IssuerName;

            //var dataToSign = XML;
            //var SignatureFieldTitle = document.getElementsByName("SignatureTitle");
            var result = [];
            var Signature;
            try {
                //XML.forEach(function(dataToSign){
                //_.each(XML, function(dataToSign){
                var i = 0;
                var errormes = "";
                try {
                    var oSigner = yield cadesplugin.CreateObjectAsync("CAdESCOM.CPSigner");
                } catch (err) {
                    errormes = "Failed to create CAdESCOM.CPSigner: " + err.message;
                    throw errormes;
                }
                if (oSigner) {
                    yield oSigner.propset_Certificate(certificate);
                    yield oSigner.Certificate = certificate;
                }
                else {
                    errormes = "Failed to create CAdESCOM.CPSigner";
                    throw errormes;
                }
                while(i < XML.length) {
                    var dataToSign = XML[i];
                    //FillCertInfo_Async(certificate);
                    var newObj = Object.assign({}, obj);
                    if (cb.SignType === "XML") {
                        var oSignedXML = yield cadesplugin.CreateObjectAsync("CAdESCOM.SignedXML");

                        var XmlDsigGost3410Url = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102001-gostr3411";
                        var XmlDsigGost3411Url = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr3411";
                        var CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED = 0;
                        var CADESCOM_XML_SIGNATURE_TYPE_ENVELOPING = 1;

                        if (dataToSign) {
                            // Данные на подпись ввели
                            yield oSignedXML.propset_Content(dataToSign);
                            if (cb.TypeOfSign) {
                                if (cb.TypeOfSign === 'ENVELOPING') {
                                    yield oSignedXML.propset_SignatureType(CADESCOM_XML_SIGNATURE_TYPE_ENVELOPING);
                                }
                                if (cb.TypeOfSign === 'ENVELOPED') {
                                    yield oSignedXML.propset_SignatureType(CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED);
                                }
                            } else {
                                yield oSignedXML.propset_SignatureType(CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED);
                            }
                            yield oSignedXML.propset_SignatureMethod(XmlDsigGost3410Url);
                            yield oSignedXML.propset_DigestMethod(XmlDsigGost3411Url);

                            try {
                                Signature = yield oSignedXML.Sign(oSigner);
                            }
                            catch (err) {
                                errormes = "Не удалось создать подпись из-за ошибки: " + err.message;
                                throw errormes;
                            }
                        }
                    } else {
                        var sHashValue = dataToSign;

                        var oHashedData = yield cadesplugin.CreateObjectAsync("CAdESCOM.HashedData");

                        var signAlgo = cadesplugin.CADESCOM_HASH_ALGORITHM_CP_GOST_3411;
                        var pubKey = yield certificate.PublicKey();
                        var algo = yield pubKey.Algorithm;
                        var algoValue = yield algo.Value;
                        if (algoValue === '1.2.643.7.1.1.1.1') {
                            signAlgo = cadesplugin.CADESCOM_HASH_ALGORITHM_CP_GOST_3411_2012_256
                        }

                        yield oHashedData.propset_Algorithm(signAlgo);

                        yield oHashedData.SetHashValue(sHashValue);

                        var oSignedData = yield cadesplugin.CreateObjectAsync("CAdESCOM.CadesSignedData");

                        var cadesProfile = resolveAdesProfile(cb);

                        if ( cb.TsaAddress ) {
                            yield oSigner.propset_TSAAddress(cb.TsaAddress);
                        }

                        try {
                            Signature = yield oSignedData.SignHash(oHashedData, oSigner, cadesProfile)
                        } catch (err) {
                            errormes = "Не удалось создать подпись " + err.message;
                            throw errormes;
                        }
                    }
                    newObj.Document = dataToSign;
                    newObj.SignedDocument = Signature;
                    newObj['Data'] = _.findWhere(cb.response, {hash: dataToSign});
                    if (_.isArray(cb.response)) {
                        newObj['Data'] = cb.response[i];
                    } else {
                        newObj['Data'] = cb.response;
                    }
                    result.push(newObj);
                    i++;
                }
                //});
                yield oStore.Close();
                result = (result.length == 1) ? result[0] : result;
                if (cb.fileSaveService === undefined) {
                    cb.successSign(result);
                } else {
                    cb.SaveService(result, cb);
                }
                document.getElementById('cert_info').classList.remove('loading')
                cb.close();
                window.application.$modals.pop();
            }
            catch (err) {
                document.getElementById('cert_info').classList.remove('loading')
                if (typeof err === 'string') {
                    cb.pluginError(err);
                } else {
                    cb.pluginError(err.message)
                }
            }
        }
        ,
        certListBoxId
    )
    ; //cadesplugin.async_spawn
}

function FillCertInfo_Async(certificate, certBoxId) {
    var BoxId;
    var field_prefix;
    if (typeof(certBoxId) == 'undefined') {
        BoxId = 'cert_info';
        field_prefix = '';
    } else {
        BoxId = certBoxId;
        field_prefix = certBoxId;
    }
    cadesplugin.async_spawn(function * (args)
        {
            var Adjust = new CertificateAdjuster();
            document.getElementById(args[1]).style.display = '';
            document.getElementById(args[2] + "subject").innerHTML = "Владелец: <b>" + Adjust.GetCertName(yield args[0].SubjectName) + "<b>";
            document.getElementById(args[2] + "issuer").innerHTML = "Издатель: <b>" + Adjust.GetIssuer(yield args[0].IssuerName) + "<b>";
            document.getElementById(args[2] + "from").innerHTML = "Выдан: <b>" + Adjust.GetCertDate(yield args[0].ValidFromDate);
            +"<b>";
            document.getElementById(args[2] + "till").innerHTML = "Действителен до: <b>" + Adjust.GetCertDate(yield args[0].ValidToDate) + "<b>";
            var pubKey = yield args[0].PublicKey();
            var algo = yield pubKey.Algorithm;
            var fAlgoName = yield algo.FriendlyName;
            var fAlgoName2 = yield algo.Name;
            var fAlgoValue = yield algo.Value;
            $("#CertListBox").data({
                Value: fAlgoValue,
                Name: fAlgoName2,
                FriendlyName: fAlgoName
            });
            document.getElementById(args[2] + "algorithm").innerHTML = "Алгоритм ключа: <b>" + fAlgoName + "<b>";
            var oPrivateKey = yield args[0].PrivateKey;
            var sProviderName = yield oPrivateKey.ProviderName;
            document.getElementById(args[2] + "provname").innerHTML = "Криптопровайдер: <b>" + sProviderName + "<b>";
        }
        ,
        certificate, BoxId, field_prefix
    )
    ;//cadesplugin.async_spawn
}

function Encrypt_Async() {
    cadesplugin.async_spawn(function * ()
        {
            document.getElementById("DataEncryptedIV1").innerHTML = "";
            document.getElementById("DataEncryptedIV2").innerHTML = "";
            document.getElementById("DataEncryptedDiversData1").innerHTML = "";
            document.getElementById("DataEncryptedDiversData2").innerHTML = "";
            document.getElementById("DataEncryptedBox1").innerHTML = "";
            document.getElementById("DataEncryptedBox2").innerHTML = "";
            document.getElementById("DataEncryptedKey1").innerHTML = "";
            document.getElementById("DataEncryptedKey2").innerHTML = "";
            document.getElementById("DataDecryptedBox1").innerHTML = "";
            document.getElementById("DataDecryptedBox2").innerHTML = "";

            //Get First certificate
            var e = document.getElementById('CertListBox1');
            var selectedCertID = e.selectedIndex;
            if (selectedCertID == -1) {
                alert("Select certificate");
                return;
            }

            var thumbprint = e.options[selectedCertID].value.split(" ").reverse().join("").replace(/\s/g, "").toUpperCase();
            try {
                var oStore = yield cadesplugin.CreateObjectAsync("CAdESCOM.Store");
                yield oStore.Open();
            } catch (err) {
                alert('Failed to create CAdESCOM.Store: ' + err.number);
                return;
            }

            var CAPICOM_CERTIFICATE_FIND_SHA1_HASH = 0;
            var all_certs = yield oStore.Certificates;
            var oCerts = yield all_certs.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, thumbprint);

            if ((yield oCerts.Count) == 0) {
                alert("Certificate not found");
                return;
            }
            var certificate1 = yield oCerts.Item(1);

            //Get second Certificate
            var e = document.getElementById('CertListBox2');
            var selectedCertID = e.selectedIndex;
            if (selectedCertID == -1) {
                alert("Select certificate");
                return;
            }

            var thumbprint = e.options[selectedCertID].value.split(" ").reverse().join("").replace(/\s/g, "").toUpperCase();
            var oCerts = yield all_certs.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, thumbprint);

            if ((yield oCerts.Count) == 0) {
                alert("Certificate not found");
                return;
            }
            var certificate2 = yield oCerts.Item(1);

            var dataToEncr1 = Base64.encode(document.getElementById("DataToEncrTxtBox1").value);
            var dataToEncr2 = Base64.encode(document.getElementById("DataToEncrTxtBox2").value);

            try {
                FillCertInfo_Async(certificate1, 'cert_info1');
                FillCertInfo_Async(certificate2, 'cert_info2');
                var errormes = "";

                try {
                    var oSymAlgo = yield cadesplugin.CreateObjectAsync("cadescom.symmetricalgorithm");
                } catch (err) {
                    errormes = "Failed to create cadescom.symmetricalgorithm: " + err;
                    alert(errormes);
                    throw errormes;
                }

                yield oSymAlgo.GenerateKey();

                var oSesKey1 = yield oSymAlgo.DiversifyKey();
                var oSesKey1DiversData = yield oSesKey1.DiversData;
                var oSesKey1IV = yield oSesKey1.IV;
                var EncryptedData1 = yield oSesKey1.Encrypt(dataToEncr1, 1);
                document.getElementById("DataEncryptedDiversData1").innerHTML = oSesKey1DiversData;
                document.getElementById("DataEncryptedIV1").innerHTML = oSesKey1IV;
                document.getElementById("DataEncryptedBox1").innerHTML = EncryptedData1;

                var oSesKey2 = yield oSymAlgo.DiversifyKey();
                var oSesKey2DiversData = yield oSesKey2.DiversData;
                var oSesKey2IV = yield oSesKey2.IV;
                var EncryptedData2 = yield oSesKey2.Encrypt(dataToEncr2, 1);
                document.getElementById("DataEncryptedDiversData2").innerHTML = oSesKey2DiversData;
                document.getElementById("DataEncryptedIV2").innerHTML = oSesKey2IV;
                document.getElementById("DataEncryptedBox2").innerHTML = EncryptedData2;

                var ExportedKey1 = yield oSymAlgo.ExportKey(certificate1);
                document.getElementById("DataEncryptedKey1").innerHTML = ExportedKey1;

                var ExportedKey2 = yield oSymAlgo.ExportKey(certificate2);
                document.getElementById("DataEncryptedKey2").innerHTML = ExportedKey2;

                alert("Данные зашифрованы успешно:");
            }
            catch (err) {
                alert("Ошибка при шифровании данных:" + err);
                throw("Ошибка при шифровании данных:" + err);
            }
        }
    )
    ;//cadesplugin.async_spawn
}

function Decrypt_Async(certListBoxId) {
    cadesplugin.async_spawn(function * (arg)
        {
            document.getElementById("DataDecryptedBox1").innerHTML = "";
            document.getElementById("DataDecryptedBox2").innerHTML = "";

            var e = document.getElementById(arg[0]);
            var selectedCertID = e.selectedIndex;
            if (selectedCertID == -1) {
                alert("Select certificate");
                return;
            }

            var thumbprint = e.options[selectedCertID].value.split(" ").reverse().join("").replace(/\s/g, "").toUpperCase();
            try {
                var oStore = yield cadesplugin.CreateObjectAsync("CAdESCOM.Store");
                yield oStore.Open();
            } catch (err) {
                alert('Failed to create CAdESCOM.Store: ' + err.number);
                return;
            }

            var CAPICOM_CERTIFICATE_FIND_SHA1_HASH = 0;
            var all_certs = yield oStore.Certificates;
            var oCerts = yield all_certs.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, thumbprint);

            if ((yield oCerts.Count) == 0) {
                alert("Certificate not found");
                return;
            }
            var certificate = yield oCerts.Item(1);

            var dataToDecr1 = document.getElementById("DataEncryptedBox1").value;
            var dataToDecr2 = document.getElementById("DataEncryptedBox2").value;
            var field;
            if (certListBoxId == 'CertListBox1')
                field = "DataEncryptedKey1";
            else
                field = "DataEncryptedKey2";

            var EncryptedKey = document.getElementById(field).value;
            try {
                FillCertInfo_Async(certificate, 'cert_info_decr');
                var errormes = "";

                try {
                    var oSymAlgo = yield cadesplugin.CreateObjectAsync("cadescom.symmetricalgorithm");
                } catch (err) {
                    errormes = "Failed to create cadescom.symmetricalgorithm: " + err;
                    alert(errormes);
                    throw errormes;
                }

                yield oSymAlgo.ImportKey(EncryptedKey, certificate);

                var oSesKey1DiversData = document.getElementById("DataEncryptedDiversData1").value;
                var oSesKey1IV = document.getElementById("DataEncryptedIV1").value;
                yield oSymAlgo.propset_DiversData(oSesKey1DiversData);
                var oSesKey1 = yield oSymAlgo.DiversifyKey();
                yield oSesKey1.propset_IV(oSesKey1IV);
                var EncryptedData1 = yield oSesKey1.Decrypt(dataToDecr1, 1);
                document.getElementById("DataDecryptedBox1").innerHTML = Base64.decode(EncryptedData1);

                var oSesKey2DiversData = document.getElementById("DataEncryptedDiversData2").value;
                var oSesKey2IV = document.getElementById("DataEncryptedIV2").value;
                yield oSymAlgo.propset_DiversData(oSesKey2DiversData);
                var oSesKey2 = yield oSymAlgo.DiversifyKey();
                yield oSesKey2.propset_IV(oSesKey2IV);
                var EncryptedData2 = yield oSesKey2.Decrypt(dataToDecr2, 1);
                document.getElementById("DataDecryptedBox2").innerHTML = Base64.decode(EncryptedData2);

                alert("Данные расшифрованы успешно:");
            }
            catch (err) {
                alert("Ошибка при шифровании данных:" + err);
                throw("Ошибка при шифровании данных:" + err);
            }
        }
        ,
        certListBoxId
    )
    ;//cadesplugin.async_spawn
}

function RetrieveCertificate_Async() {
    cadesplugin.async_spawn(function * (arg)
        {
            try {
                var PrivateKey = yield cadesplugin.CreateObjectAsync("X509Enrollment.CX509PrivateKey");
            }
            catch (e) {
                alert('Failed to create X509Enrollment.CX509PrivateKey: ' + cadesplugin.getLastError(e));
                return;
            }

            yield PrivateKey.propset_ProviderName("Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider");
            yield PrivateKey.propset_ProviderType(75);
            yield PrivateKey.propset_KeySpec(1); //XCN_AT_KEYEXCHANGE

            try {
                var CertificateRequestPkcs10 = yield cadesplugin.CreateObjectAsync("X509Enrollment.CX509CertificateRequestPkcs10");
            }
            catch (e) {
                alert('Failed to create X509Enrollment.CX509CertificateRequestPkcs10: ' + cadesplugin.getLastError(e));
                return;
            }

            yield CertificateRequestPkcs10.InitializeFromPrivateKey(0x1, PrivateKey, "");

            try {
                var DistinguishedName = yield cadesplugin.CreateObjectAsync("X509Enrollment.CX500DistinguishedName");
            }
            catch (e) {
                alert('Failed to create X509Enrollment.CX500DistinguishedName: ' + cadesplugin.getLastError(e));
                return;
            }

            var CommonName = "Test Certificate";
            yield DistinguishedName.Encode("CN=\"" + CommonName.replace(/"/g, "\"\"") + "\";");

            yield CertificateRequestPkcs10.propset_Subject(DistinguishedName);

            var KeyUsageExtension = yield cadesplugin.CreateObjectAsync("X509Enrollment.CX509ExtensionKeyUsage");
            var CERT_DATA_ENCIPHERMENT_KEY_USAGE = 0x10;
            var CERT_KEY_ENCIPHERMENT_KEY_USAGE = 0x20;
            var CERT_DIGITAL_SIGNATURE_KEY_USAGE = 0x80;
            var CERT_NON_REPUDIATION_KEY_USAGE = 0x40;

            yield KeyUsageExtension.InitializeEncode(
                CERT_KEY_ENCIPHERMENT_KEY_USAGE |
                CERT_DATA_ENCIPHERMENT_KEY_USAGE |
                CERT_DIGITAL_SIGNATURE_KEY_USAGE |
                CERT_NON_REPUDIATION_KEY_USAGE);

            yield (yield CertificateRequestPkcs10.X509Extensions).Add(KeyUsageExtension);

            try {
                var Enroll = yield cadesplugin.CreateObjectAsync("X509Enrollment.CX509Enrollment");
            }
            catch (e) {
                alert('Failed to create X509Enrollment.CX509Enrollment: ' + cadesplugin.getLastError(e));
                return;
            }

            yield Enroll.InitializeFromRequest(CertificateRequestPkcs10);

            var cert_req = yield Enroll.CreateRequest(0x1);

            var params = 'CertRequest=' + encodeURIComponent(cert_req) +
                '&Mode=' + encodeURIComponent('newreq') +
                '&TargetStoreFlags=' + encodeURIComponent('0') +
                '&SaveCert=' + encodeURIComponent('no');

            var xmlhttp = getXmlHttp();
            xmlhttp.open("POST", "https://www.cryptopro.ru/certsrv/certfnsh.asp", true);
            xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            var response;
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    if (xmlhttp.status == 200) {
                        cadesplugin.async_spawn(function * (arg)
                            {
                                var response = arg[0];
                                var cert_data = "";

                                if (!isIE()) {
                                    var start = response.indexOf("var sPKCS7");
                                    var end = response.indexOf("sPKCS7 += \"\"") + 13;
                                    cert_data = response.substring(start, end);
                                }
                                else {
                                    var start = response.indexOf("sPKCS7 & \"") + 9;
                                    var end = response.indexOf("& vbNewLine\r\n\r\n</Script>");
                                    cert_data = response.substring(start, end);
                                    cert_data = cert_data.replace(new RegExp(" & vbNewLine", 'g'), ";");
                                    cert_data = cert_data.replace(new RegExp("&", 'g'), "+");
                                    cert_data = "var sPKCS7=" + cert_data + ";";
                                }

                                eval(cert_data);

                                try {
                                    var Enroll = yield cadesplugin.CreateObjectAsync("X509Enrollment.CX509Enrollment");
                                }
                                catch (e) {
                                    alert('Failed to create X509Enrollment.CX509Enrollment: ' + cadesplugin.getLastError(e));
                                    return;
                                }

                                yield Enroll.Initialize(0x1);
                                yield Enroll.InstallResponse(0, sPKCS7, 0x7, "");
                                var errormes = document.getElementById("boxdiv").style.display = 'none';
                                if (location.pathname.indexOf("simple") >= 0) {
                                    location.reload();
                                }
                                else if (location.pathname.indexOf("symalgo_sample.html") >= 0) {
                                    FillCertList_Async('CertListBox1');
                                    FillCertList_Async('CertListBox2');
                                }
                                else {
                                    FillCertList_Async('CertListBox');
                                }
                            }
                            ,
                            xmlhttp.responseText
                        )
                        ;//cadesplugin.async_spawn
                    }
                }
            }
            xmlhttp.send(params);
        }
    )
    ;//cadesplugin.async_spawn
}

function createSignature_Async() {
    return new Promise(function (resolve, reject) {
        cadesplugin.async_spawn(function * (args)
            {
                var signedMessage = "";
                try {
                    var oSigner = yield cadesplugin.CreateObjectAsync("CAdESCOM.CPSigner");
                    yield oSigner.propset_Certificate(g_oCert);
                    var CAPICOM_CERTIFICATE_INCLUDE_WHOLE_CHAIN = 1;
                    yield oSigner.propset_Options(CAPICOM_CERTIFICATE_INCLUDE_WHOLE_CHAIN);

                    var oSignedData = yield cadesplugin.CreateObjectAsync("CAdESCOM.CadesSignedData");
                    yield oSignedData.propset_Content("DataToSign");

                    var CADES_BES = 1;
                    signedMessage = yield oSignedData.SignCades(oSigner, CADES_BES);
                    args[0](signedMessage);
                }
                catch (e) {
                    showErrorMessage("Ошибка: Не удалось создать подпись. Код ошибки: " + cadesplugin.getLastError(e));
                    args[1]("");
                }
                args[0](signedMessage);
            }
            ,
            resolve, reject
        )
        ;
    });
}

function isIE() {
    var retVal = (("Microsoft Internet Explorer" == navigator.appName) || // IE < 11
        navigator.userAgent.match(/Trident\/./i)); // IE 11
    return retVal;
}

function resolveAdesProfile(options) {
    switch (options.AdesProfile) {
        case "BES":
            return cadesplugin.CADESCOM_CADES_BES;
        case "TIMESTAMP":
            return cadesplugin.CADESCOM_CADES_T;
        case "X_LONG_TYPE":
            return cadesplugin.CADESCOM_CADES_X_LONG_TYPE_1;
        default:
            return cadesplugin.CADESCOM_CADES_BES;
    }
}


