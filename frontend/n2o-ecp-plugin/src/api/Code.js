import { FillCertListNumber_Async } from './asyncCode';

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

export default CodeApi;