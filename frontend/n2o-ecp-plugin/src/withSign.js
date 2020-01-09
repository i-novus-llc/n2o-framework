import compose from "recompose/compose";
import defaultProps from "recompose/defaultProps";
import lifecycle from "recompose/lifecycle";
import withState from "recompose/withState";
import withHandlers from "recompose/withHandlers";

import { SignType } from "./constants";
import EcpApi from "./EcpApi";

const enhance = compose(
  defaultProps({
    signType: SignType.HASH,
    successSign: () => {},
    errorSign: () => {}
  }),
  withState("loading", "setLoading", false),
  withState("certificates", "setCertificates", []),
  withState("certificate", "setCertificate"),
  withState("isOpen", "setOpen", ({ isOpen }) => isOpen),
  withState("error", "setError", false),
  withState("success", "setSuccess", false),
  withHandlers({
    toggle: ({ isOpen, setOpen }) => () => setOpen(!isOpen),
    createAlert: () => (label, severity, onDismiss) => [
      {
        key: 1,
        position: "absolute",
        severity,
        label,
        animate: true,
        onDismiss: () => onDismiss(false)
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
      errorSign,
      setLoading,
      setOpen,
      setSuccess
    }) => async () => {
      try {
        setLoading(true);
        const signedData = await EcpApi.sign({
          signType,
          certificate,
          data: fileForSign,
          typeOfSign,
          fileRequestService,
          fileSaveService
        });

        successSign(signedData);
        setSuccess(true);
      } catch (e) {
        console.error(e);
        setError(e);

        errorSign(e);
      } finally {
        setLoading(false);
        // setOpen(false);
      }
    }
  }),
  lifecycle({
    async componentDidUpdate(prevProps) {
      const {
        setError,
        isOpen,
        setCertificates,
        setCertificate,
        setLoading
      } = this.props;

      if (!prevProps.isOpen && isOpen) {
        try {
          setLoading(true);
          const certificates = await EcpApi.getCertificates();

          setCertificates(certificates);

          if (certificates.length === 1) {
            setCertificate(certificates[0]);
          }
        } catch (e) {
          setError(e);
        } finally {
          setLoading(false);
        }
      }
    }
  })
);

export default enhance;
