import CryptoPro from "crypto-pro";

import isFunction from "lodash/isFunction";
import set from "lodash/set";

import queryString from "query-string";

import { SignType } from "./constants";

class EcpApi {
  static getCertificates() {
    return CryptoPro.call("getCertsList");
  }

  static getCertificate(hash) {
    return CryptoPro.call("getCert", hash);
  }

  static getDocumentBeforeSign({
    url,
    type = "POST",
    data,
    documentKey = "hash"
  }) {
    const options = {
      method: type
    };

    if (type === "GET") {
      const query = queryString.stringify(isFunction(data) ? data() : data);

      url += `?${query}`;
    } else {
      set(options, "body", JSON.stringify(isFunction(data) ? data() : data));
    }

    return fetch(url, options).then(response => {
      try {
        const json = response.json();

        return json[documentKey];
      } catch (e) {
        return response.responseText;
      }
    });
  }

  static saveDocumentAfterSign(
    { url, type = "POST", data, dataKey = "data" },
    signedData
  ) {
    const options = {
      method: type
    };
    const body = {
      [dataKey]: signedData,
      ...data
    };

    if (type === "GET") {
      const query = queryString.stringify(body);
      url += `?${query}`;
    } else {
      set(options, "body", JSON.stringify(body));
    }

    return fetch(url, options);
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

    if (signType === SignType.HASH) {
      signedData = await CryptoPro.call(
        "signData",
        hash,
        btoa(data),
        typeOfSign
      );
    } else {
      signedData = await CryptoPro.call("signDataXml", hash, data);
    }

    if (fileSaveService) {
      await EcpApi.saveDocumentAfterSign(fileSaveService, signedData);
    }

    return signedData;
  }
}

export default EcpApi;
