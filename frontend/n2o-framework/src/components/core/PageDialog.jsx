import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import Modal from 'reactstrap/lib/Modal'
import ModalHeader from 'reactstrap/lib/ModalHeader'
import ModalBody from 'reactstrap/lib/ModalBody'
import ModalFooter from 'reactstrap/lib/ModalFooter'
import classNames from 'classnames'

import Toolbar from '../buttons/Toolbar'

import withOverlayMethods from './withOverlayMethods'

function PageDialog({ visible, props }) {
    const { title, text, size, scrollable } = props

    return (
        <div className="modal-page-overlay">
            <Modal
                isOpen={visible}
                size={size}
                scrollable={scrollable}
                modalClassName={classNames({
                    'simple-modal-dialog': !title && !text,
                })}
            >
                {title && (
                    <ModalHeader
                        className={{ 'modal-page-overlay--border-bottom-none': !text }}
                    >
                        {title}
                    </ModalHeader>
                )}
                {text && (
                    <ModalBody className="white-space-pre-line">{text}</ModalBody>
                )}
                <ModalFooter
                    className={{ 'modal-page-overlay--border-top-none': !text }}
                >
                    <Toolbar
                        className="mr-auto"
                        entityKey="dialog"
                        toolbar={get(props, 'toolbar.bottomLeft')}
                    />
                    <Toolbar
                        className="mr-auto"
                        entityKey="dialog"
                        toolbar={get(props, 'toolbar.bottomCenter')}
                    />
                    <Toolbar
                        className="ml-auto"
                        entityKey="dialog"
                        toolbar={get(props, 'toolbar.bottomRight')}
                    />
                </ModalFooter>
            </Modal>
        </div>
    )
}

PageDialog.propTypes = {
    visible: PropTypes.bool,
    props: PropTypes.object,
    title: PropTypes.string,
    text: PropTypes.string,
    size: PropTypes.string,
    scrollable: PropTypes.bool,
}

PageDialog.defaultProps = {
    props: {},
    size: 'sm',
    scrollable: false,
}

export default withOverlayMethods(PageDialog)
