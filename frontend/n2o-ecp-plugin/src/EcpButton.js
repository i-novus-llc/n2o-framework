import React from "react";
import PropTypes from "prop-types";

import split from "lodash/split";
import get from "lodash/get";
import first from "lodash/first";

import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import Alerts from "n2o-framework/lib/components/snippets/Alerts/Alerts";
import Collapse, {
  Panel
} from "n2o-framework/lib/components/snippets/Collapse/Collapse";

import withSign from "./withSign";
import { SignType } from "./constants";

function EcpButton({
  buttonLabel,
  title,
  signButtonLabel,
  cancelButtonLabel,
  toggle,
  isOpen,
  certificates,
  error,
  setError,
  createAlert,
  certificate,
  setCertificate,
  loading,
  sign,
  success,
  setOpen,
  alertSuccessMessage
}) {
  return (
    <div className="n2o-ecp-plugin">
      <Button onClick={toggle}>{buttonLabel}</Button>
      <Modal toggle={toggle} isOpen={isOpen}>
        {error && (
          <Alerts alerts={createAlert(error.toString(), "danger", setError)} />
        )}
        {success && (
          <Alerts
            alerts={createAlert(alertSuccessMessage, "success", setOpen)}
          />
        )}
        <ModalHeader toggle={toggle}>{title}</ModalHeader>
        <ModalBody>
          <Collapse>
            <Panel
              disabled={!certificate}
              className="mb-2"
              key="1"
              header="Информация о сертификате"
              type="default"
            >
              <div>
                <div>
                  Владелец: <strong>{get(certificate, "subjectName")}</strong>
                </div>
                <div>
                  Издатель:{" "}
                  <strong>
                    {first(split(get(certificate, "issuerName"), ","))}
                  </strong>
                </div>
                <div>
                  Выдан: <strong>{get(certificate, "validFrom")}</strong>
                </div>
                <div>
                  Дейтвителен до: <strong>{get(certificate, "validTo")}</strong>
                </div>
                <div>
                  Алгоритм: <strong>{get(certificate, "algorithm")}</strong>
                </div>
              </div>
            </Panel>
          </Collapse>
          <InputSelect
            disabled={certificates.length === 0}
            loading={!error && loading}
            placeholder="Выберите сертификат"
            options={certificates}
            valueFieldId="thumbprint"
            labelFieldId="subjectName"
            value={certificate}
            onChange={setCertificate}
          />
        </ModalBody>
        <ModalFooter className="d-flex justify-content-between">
          <Button onClick={sign} color="primary">
            {signButtonLabel}
          </Button>
          <Button onClick={toggle}>{cancelButtonLabel}</Button>
        </ModalFooter>
      </Modal>
    </div>
  );
}

EcpButton.propTypes = {
  buttonLabel: PropTypes.string,
  title: PropTypes.string,
  alertSuccessMessage: PropTypes.string,
  signButtonLabel: PropTypes.string,
  cancelButtonLabel: PropTypes.string,
  fileRequestService: PropTypes.shape({
    url: PropTypes.string,
    type: PropTypes.string,
    data: PropTypes.oneOfType([PropTypes.object, PropTypes.func]),
    documentKey: PropTypes.string
  }),
  fileForSign: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
  fileSaveService: PropTypes.shape({
    url: PropTypes.string,
    type: PropTypes.string,
    data: PropTypes.oneOfType([PropTypes.object, PropTypes.func])
  }),
  signType: PropTypes.oneOf([SignType.XML, SignType.HASH]),
  typeOfSign: PropTypes.bool,
  successSign: PropTypes.func,
  errorSign: PropTypes.func
};

EcpButton.defaultProps = {
  buttonLabel: "Подписать",
  title: "Электронная подпись",
  alertSuccessMessage: "Пакет электронных документов успешно подписан",
  signButtonLabel: "Подписать",
  cancelButtonLabel: "Закрыть",
  signType: SignType.HASH,
  successSign: () => {},
  errorSign: () => {}
};

export { EcpButton };
export default withSign(EcpButton);
