import CryptoPro from "crypto-pro";

import isFunction from "lodash/isFunction";

import { SignType } from "./constants";

class EcpApi {
  static getCertificates() {
    return CryptoPro.call("getCertsList");
  }

  static getCertificate(hash) {
    return CryptoPro.call("getCert", hash);
  }

  static getDocumentBeforeSign({ url, type, data, documentKey = "hash" }) {
    return fetch(url, {
      method: type,
      body: isFunction(data) ? data() : data
    }).then(response => {
      try {
        const json = response.json();

        return json[documentKey];
      } catch (e) {
        return response.responseText;
      }
    });
  }

  static saveDocumentAfterSign({ url, type, data }, signedData) {
    return fetch(url, {
      method: type,
      body: isFunction(data) ? data(signedData) : signedData
    });
  }

  static async sign({
    signType,
    hash,
    data,
    typeOfSign,
    fileRequestService,
    fileSaveService
  }) {
    let signedData;

    if (fileRequestService) {
      data = await EcpApi.getDocumentBeforeSign(fileRequestService);
    }

    if (signType === SignType.XML) {
      signedData = await CryptoPro.call("signDataXml", hash, data);
    } else {
      signedData = await CryptoPro.call(
        "signData",
        hash,
        btoa(data),
        typeOfSign
      );
    }

    if (fileSaveService) {
      await EcpApi.saveDocumentAfterSign(fileSaveService);
    }

    return signedData;
  }
}

export default EcpApi;
