import React from 'react'
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

const content = [{ id: '1', name: 'Сертификат',}];

function EcpComponent({
    toggle,
    isOpen,
    content = [],
    error,
    createAlert,
    plugin
}) {
  return (
      <div className="n2o-ecp-plugin">
          <Button onClick={toggle}>Open ECP</Button>
          <Modal isOpen={isOpen}>
              {error && <Alerts alerts={createAlert(error, 'danger')} />}
              <ModalHeader>N2O ECP plugin</ModalHeader>
              <ModalBody>
                  <InputSelect options={content} />
              </ModalBody>
              <ModalFooter className='d-flex justify-content-between'>
                  <Button>Подписать</Button>
                  <Button onClick={toggle}>Отмена</Button>
              </ModalFooter>
          </Modal>
      </div>
  );
}

export default compose(
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
            const { setError, isOpen } = this.props;
            const { plugin } = this.state;

            if (plugin && prevProps.isOpen !== isOpen) {
                plugin.hasCertificates()
                    .catch(setError);
            }
        }
    })
)(EcpComponent)
