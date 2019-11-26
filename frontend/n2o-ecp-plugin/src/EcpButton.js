import React from "react";
import PropTypes from "prop-types";

import split from "lodash/split";
import get from "lodash/get";
import first from "lodash/first";
import isFunction from "lodash/isFunction";

import compose from "recompose/compose";
import withState from "recompose/withState";
import withHandlers from "recompose/withHandlers";
import lifecycle from "recompose/lifecycle";

import Modal from "reactstrap/lib/Modal";
import ModalHeader from "reactstrap/lib/ModalHeader";
import ModalBody from "reactstrap/lib/ModalBody";
import ModalFooter from "reactstrap/lib/ModalFooter";
import Button from "reactstrap/lib/Button";
import InputSelect from "n2o-framework/lib/components/controls/InputSelect/InputSelect";
import Alerts from "n2o-framework/lib/components/snippets/Alerts/Alerts";
import Collapse, {
  Panel
} from "n2o-framework/lib/components/snippets/Collapse/Collapse";

import { SignType, TypeOfSign } from "./constants";
import EcpApi from "./EcpApi";

function EcpButton({
  buttonLabel,
  title,
  signButtonLabel,
  cancelButtonLabel,
  toggle,
  isOpen,
  certificates,
  error,
  createAlert,
  certificate,
  setCertificate,
  loading,
  sign
}) {
  return (
    <div className="n2o-ecp-plugin">
      <Button onClick={toggle}>{buttonLabel}</Button>
      <Modal toggle={toggle} isOpen={isOpen}>
        {error && <Alerts alerts={createAlert(error.toString(), "danger")} />}
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
  signButtonLabel: "Подписать",
  cancelButtonLabel: "Отмена",
  signType: SignType.HASH,
  successSign: () => {},
  errorSign: () => {}
};

const enhance = compose(
  withState("loading", "setLoading", false),
  withState("certificates", "setCertificates", []),
  withState("certificate", "setCertificate"),
  withState("isOpen", "setOpen", ({ isOpen }) => isOpen),
  withState("error", "setError", false),
  withHandlers({
    toggle: ({ isOpen, setOpen }) => () => {
      setOpen(!isOpen);
    },
    createAlert: () => (label, severity) => [
      {
        key: 1,
        position: "absolute",
        severity,
        label,
        closeButton: false
      }
    ],
    sign: ({
      signType,
      certificate,
      fileForSign,
      typeOfSign,
      fileRequestService,
      fileSaveService,
      setError,
      successSign,
      errorSign
    }) => () => {
      EcpApi.sign({
        signType,
        hash: certificate.thumbprint,
        data: fileForSign,
        typeOfSign,
        fileRequestService,
        fileSaveService
      })
        .then(signedData => {
          console.log(signedData);
          if (isFunction(successSign)) successSign(signedData);
        })
        .catch(err => {
          setError(err);
          console.log(err);
          if (isFunction(errorSign)) errorSign(err);
        });
    }
  }),
  lifecycle({
    componentDidUpdate(prevProps) {
      const {
        setError,
        isOpen,
        setCertificates,
        setCertificate,
        setLoading
      } = this.props;

      if (prevProps.isOpen !== isOpen) {
        setLoading(true);
        EcpApi.getCertificates()
          .then(certificates => {
            setCertificates(certificates);

            if (certificates.length === 1) {
              setCertificate(certificates[0]);
            }
          })
          .then(() => setLoading(false))
          .catch(setError);
      }
    }
  })
);

export { EcpButton };
export default enhance(EcpButton);
