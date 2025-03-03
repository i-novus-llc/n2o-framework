import React from 'react'
import { useSelector } from 'react-redux'
import get from 'lodash/get'
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import Toolbar, { ToolbarProps } from '../buttons/Toolbar'
import { getGlobalFieldByPath } from '../../ducks/models/selectors'
import propsResolver from '../../utils/propsResolver'

import withOverlayMethods from './withOverlayMethods'

interface Props {
    visible: boolean
    props: {
        title?: string
        text?: string
        size?: 'sm' | 'lg' | 'xl'
        scrollable?: boolean
        modelLink: string
        toolbar?: {
            bottomLeft?: ToolbarProps
            bottomCenter?: ToolbarProps
            bottomRight?: ToolbarProps
        };
    };
}

const usePageDialog = ({ modelLink, ...props }: Props['props']) => {
    const model = useSelector(getGlobalFieldByPath(modelLink))

    return model ? propsResolver(props, model) : props
}

const PageDialog = ({ visible, props }: Props) => {
    const { title, text, size = 'sm', scrollable = false } = usePageDialog(props)

    return (
        <div className="modal-page-overlay">
            <Modal
                isOpen={visible}
                size={size}
                scrollable={scrollable}
                modalClassName={classNames({ 'simple-modal-dialog': !title && !text })}
            >
                {title && (
                    <ModalHeader className={classNames({ 'modal-page-overlay--border-bottom-none': !text })}>
                        <Text>{title}</Text>
                    </ModalHeader>
                )}

                {text && <ModalBody className="white-space-pre-line"><Text>{text}</Text></ModalBody>}

                <ModalFooter className={classNames({ 'modal-page-overlay--border-top-none': !text })}>
                    <Toolbar className="mr-auto" entityKey="dialog" toolbar={get(props, 'toolbar.bottomLeft')} />
                    <Toolbar className="mr-auto" entityKey="dialog" toolbar={get(props, 'toolbar.bottomCenter')} />
                    <Toolbar className="ml-auto" entityKey="dialog" toolbar={get(props, 'toolbar.bottomRight')} />
                </ModalFooter>
            </Modal>
        </div>
    )
}

export default withOverlayMethods(PageDialog)
