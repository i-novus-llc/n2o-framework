import CryptoPro from "crypto-pro";

import isFunction from "lodash/isFunction";
import isArray from "lodash/isArray";
import isObject from "lodash/isObject";
import map from "lodash/map";
import find from "lodash/find";
import get from "lodash/get";
import set from "lodash/set";

import queryString from "query-string";

import { SignType } from "./constants";

class EcpApi {
  /**
   * Получение списка сертификатов
   * @return {Promise}
   */
  static async getCertificates() {
    let certificates = await CryptoPro.call("getCertsList");

    certificates = await Promise.all(
      map(certificates, async cert =>
        Object.assign({}, cert, await cert.getAlgorithm(), {
          serialNumber: await cert._cert.SerialNumber
        })
      )
    );

    return certificates;
  }

  /**
   * Получение сертификата по хешу
   * @param hash - хеш сертификата
   * @return {Promise<*>}
   */
  static async getCertificate(hash) {
    let cert = await CryptoPro.call("getCert", hash);
    cert = Object.assign({}, cert, await cert.getAlgorithm(), {
      serialNumber: await cert._cert.SerialNumber
    });

    return cert;
  }

  /**
   * Получение документа с сервера
   * @param url
   * @param type - метод запроса
   * @param data - данные, которые отправятся вместе с запросом
   * @param certificate - сертифкат
   * @return {Promise<Response>}
   */
  static getDocumentBeforeSign({ url, type = "GET", data }, certificate) {
    const options = {
      method: type
    };

    const body = Object.assign({}, isFunction(data) ? data() : data, {
      "certificate[Value]": certificate.oid,
      "certificate[Name]": certificate.name,
      "certificate[FriendlyName]": certificate.algorithm
    });

    if (type === "GET") {
      const query = queryString.stringify(body);

      url += `?${query}`;
    } else {
      set(options, "body", body);
    }

    return fetch(url, options).then(response => {
      try {
        return response.json();
      } catch (e) {
        return response.responseText;
      }
    });
  }

  /**
   * Создание даты в формате YYYY.MM.DD hh:mm:ss
   * @return {string}
   */
  static getSignDate() {
    const date = new Date();

    return `${date.getFullYear()}.${date.getMonth()}.${date.getDate()} ${date.getMinutes()}:${date.getSeconds()}`;
  }

  /**
   * Подготовка данных к отправке на сервер
   * @param certificate - сертификат подписи
   * @param signedData - массивы подписей
   * @param document - документ
   * @param documentKey - ключ, данных из документа
   * @return {{SerialNumber: *, Issuer: *, ValidTo: *, SignedDocument: *, ValidFrom: *, Data: *, SignDate: string, Document: (*), Name: *}}
   */
  static prepareBodyToSave(certificate, signedData, document, documentKey) {
    return {
      Data: document,
      Document: isObject(document) ? document[documentKey] : document,
      Issuer: certificate.issuerName,
      Name: certificate.subjectName,
      SerialNumber: certificate.serialNumber,
      SignDate: this.getSignDate(),
      SignedDocument: signedData,
      ValidFrom: certificate.validFrom,
      ValidTo: certificate.validTo
    };
  }

  /**
   * Сохранение подписанных данных
   * @param url
   * @param type - метод запроса
   * @param data - данные, которые отправяться вместе с запросом
   * @param document - документ
   * @param signedData - подписи
   * @param certificate - сертификат
   * @param documentKey - ключ, данных из документа
   * @return {Promise<Response>}
   */
  static async saveDocumentAfterSign(
    { url, type = "POST", data },
    document,
    signedData,
    certificate,
    documentKey
  ) {
    const options = {
      method: type
    };
    let body = {};

    if (isArray(signedData)) {
      body = map(document, doc => {
        const sign = get(
          find(signedData, ({ hash }) => hash === doc[documentKey]),
          "sign"
        );

        return this.prepareBodyToSave(certificate, sign, doc, documentKey);
      });
    } else {
      body = this.prepareBodyToSave(
        certificate,
        signedData,
        document,
        documentKey
      );
    }

    if (type === "GET") {
      const query = queryString.stringify(body);
      url += `?${query}`;
    } else {
      set(options, "body", JSON.stringify(body));
    }

    return fetch(url, options);
  }

  /**
   * Метод подписи
   * @param signType - формат подписи (HASH / XML)
   * @param certificate - сертификат
   * @param data - данные для подписи
   * @param typeOfSign - тип подписи (открепленная / присоединенная)
   * @param fileRequestService - настройка получения данных для подписи
   * @param fileSaveService - настройка сохранения подписанных документов
   * @return {Promise}
   */
  static async sign({
    signType,
    certificate,
    data,
    typeOfSign,
    fileRequestService,
    fileSaveService
  }) {
    let signedData;
    const documentKey = get(fileRequestService, "documentKey", "hash");

    if (fileRequestService) {
      data = await this.getDocumentBeforeSign(fileRequestService, certificate);
    }

    if (signType === SignType.HASH) {
      if (isArray(data)) {
        signedData = await Promise.all(
          map(data, async document => ({
            hash: document[documentKey],
            sign: await CryptoPro.call(
              "signData",
              certificate.thumbprint,
              btoa(document[documentKey]),
              typeOfSign
            )
          }))
        );
      } else {
        signedData = await CryptoPro.call(
          "signData",
          certificate.thumbprint,
          btoa(isObject(data) && fileRequestService ? data[documentKey] : data),
          typeOfSign
        );
      }
    } else {
      signedData = await CryptoPro.call(
        "signDataXml",
        certificate.thumbprint,
        data
      );
    }

    if (fileSaveService) {
      await this.saveDocumentAfterSign(
        fileSaveService,
        data,
        signedData,
        certificate,
        documentKey
      );
    }

    return signedData;
  }
}

export default EcpApi;
