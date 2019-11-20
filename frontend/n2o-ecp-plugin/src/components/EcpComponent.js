import React from 'react'
import PropTypes from 'prop-types';

import compose from 'recompose/compose';
import withState from 'recompose/withState';
import withHandlers from 'recompose/withHandlers';
import lifecycle from 'recompose/lifecycle';

import Modal from 'reactstrap/lib/Modal';
import ModalHeader from 'reactstrap/lib/ModalHeader';
import ModalBody from 'reactstrap/lib/ModalBody';
import ModalFooter from 'reactstrap/lib/ModalFooter';
import Button from 'reactstrap/lib/Button';
import InputSelect from 'n2o-framework/lib/components/controls/InputSelect/InputSelect';
import Alerts from 'n2o-framework/lib/components/snippets/Alerts/Alerts';

import EcpApi from '../api/EcpApi';

function EcpComponent({
    buttonLabel,
    title,
    signButtonLabel,
    cancelButtonLabel,
    toggle,
    isOpen,
    data,
    error,
    createAlert,
}) {
  return (
      <div className="n2o-ecp-plugin">
          <Button onClick={toggle}>{buttonLabel}</Button>
          <Modal
              toggle={toggle}
              isOpen={isOpen}
          >
              {error && <Alerts alerts={createAlert(error, 'danger')} />}
              <ModalHeader toggle={toggle}>{title}</ModalHeader>
              <ModalBody>
                  <InputSelect
                      disabled={data.length === 0}
                      placeholder='Выберите сертификат'
                      options={data}
                      valueFieldId='value'
                      labelFieldId='text'
                  />
              </ModalBody>
              <ModalFooter className='d-flex justify-content-between'>
                  <Button>{signButtonLabel}</Button>
                  <Button onClick={toggle}>{cancelButtonLabel}</Button>
              </ModalFooter>
          </Modal>
      </div>
  );
}

EcpComponent.propTypes = {
    buttonLabel: PropTypes.string,
    title: PropTypes.string,
    signButtonLabel: PropTypes.string,
    cancelButtonLabel: PropTypes.string,
};

EcpComponent.defaultProps = {
    buttonLabel: 'Подписать',
    title: 'Электронная подпись',
    signButtonLabel: 'Подписать',
    cancelButtonLabel: 'Отмена',
};

export default compose(
    withState('data', 'setData', []),
    withState('isOpen', 'setOpen', ({ isOpen }) => isOpen),
    withState('error', 'setError', false),
    withHandlers({
        toggle: ({ isOpen, setOpen }) => () => {
            setOpen(!isOpen);
        },
        createAlert: () => (label, severity) => ([{
            key: 1,
            position: 'absolute',
            severity,
            label,
            closeButton: false
        }]),
    }),
    lifecycle({
        componentDidMount() {
            const plugin = new EcpApi();

            this.setState({ plugin });
        },
        async componentDidUpdate(prevProps) {
            const { setError, isOpen, setData } = this.props;
            const { plugin } = this.state;

            if (plugin && prevProps.isOpen !== isOpen) {
                plugin.hasCertificates()
                    .then(() =>
                        plugin.getCertificates()
                            .then(setData))
                    .catch(setError);
            }
        }
    })
)(EcpComponent)
