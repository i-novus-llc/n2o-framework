import React from 'react'
import { useSelector } from 'react-redux'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'

import Toolbar from '../buttons/Toolbar'
import { getGlobalFieldByPath } from '../../ducks/models/selectors'
import propsResolver from '../../utils/propsResolver'

import withOverlayMethods from './withOverlayMethods'

const usePageDialog = ({ modelLink, ...props }) => {
    const model = useSelector(getGlobalFieldByPath(modelLink))

    return model ? propsResolver(props, model) : props
}

function PageDialog({ visible, props = {} }) {
    const { title, text, size = 'sm', scrollable = false } = usePageDialog(props)

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
    props: PropTypes.shape({
        title: PropTypes.string,
        text: PropTypes.string,
        size: PropTypes.string,
        scrollable: PropTypes.bool,
        modelLink: PropTypes.string,
    }),
}

export default withOverlayMethods(PageDialog)
