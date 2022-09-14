import React from 'react'
import PropTypes from 'prop-types'

import { PopoverConfirm } from '../snippets/PopoverConfirm/PopoverConfirm'
import ModalDialog from '../actions/ModalDialog/ModalDialog'
import { TooltipHOC } from '../snippets/Tooltip/TooltipHOC'

const ConfirmMode = {
    POPOVER: 'popover',
    MODAL: 'modal',
}

function ActionButtonBody({
    Component,
    componentProps,
    url,
    componentDisabled,
    componentVisible,
    onClick,
    id,
    confirmMode,
    isOpen,
    onConfirm,
    onDeny,
    confirmProps,
    confirmTarget,
    confirmVisible,
    close,
}) {
    return (
        <div>
            <Component
                {...componentProps}
                url={url}
                disabled={componentDisabled}
                visible={componentVisible}
                onClick={onClick}
                id={id}
            />
            {
                confirmMode === ConfirmMode.POPOVER
                    ? (
                        <PopoverConfirm
                            {...confirmProps}
                            isOpen={isOpen}
                            onConfirm={onConfirm}
                            onDeny={onDeny}
                            target={confirmTarget}
                        />
                    )
                    : null
            }
            {
                confirmMode === ConfirmMode.MODAL
                    ? (
                        <ModalDialog
                            {...confirmProps}
                            visible={confirmVisible}
                            onConfirm={onConfirm}
                            onDeny={onDeny}
                            close={close}
                        />
                    )
                    : null
            }
        </div>
    )
}

ActionButtonBody.propTypes = {
    Component: PropTypes.node,
    componentProps: PropTypes.object,
    url: PropTypes.string,
    componentDisabled: PropTypes.bool,
    componentVisible: PropTypes.bool,
    onClick: PropTypes.func,
    id: PropTypes.string,
    confirmMode: PropTypes.string,
    isOpen: PropTypes.bool,
    onConfirm: PropTypes.func,
    onDeny: PropTypes.func,
    confirmProps: PropTypes.object,
    confirmTarget: PropTypes.string,
    confirmVisible: PropTypes.func,
    close: PropTypes.func,
}

export const ActionButton = TooltipHOC(ActionButtonBody)
