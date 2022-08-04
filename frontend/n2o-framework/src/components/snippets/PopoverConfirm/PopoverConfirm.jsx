import React from 'react'
import PropTypes from 'prop-types'
import { useTranslation } from 'react-i18next'
import { Popover, PopoverHeader, PopoverBody, Button, ButtonGroup } from 'reactstrap'
import classNames from 'classnames'

/**
 * PopoverConfirm
 * @reactProps {string} className - className компонента
 * @reactProps {string} title - заголовок PopoverConfirm
 * @reactProps {object} ok - конфиг кнопки подтверждения
 * @reactProps {string} ok.label - текст кнопки подтверждения
 * @reactProps {string} ok.color - цвет кнопки подтверждения
 * @reactProps {object} cancel - конфиг кнопки отмены
 * @reactProps {string} cancel.label - текст кнопки отмены
 * @reactProps {string} cancel.color - цвет кнопки отмены
 * @reactProps {string} text - текст сообщения
 * @reactProps {string} target - target element id popover
 * @example
 * <PopoverConfirm title="are you sure?" okLabel="ok" cancelLabel="no" />
 */

export function PopoverConfirm(props) {
    const { t } = useTranslation()
    const {
        className,
        title,
        text,
        ok,
        cancel,
        target,
        onDeny,
        onConfirm,
        isOpen,
        reverseButtons = false,
        ...rest
    } = props

    const { label: okLabel = t('confirm'), color: okColor = 'primary' } = ok || {}
    const { label: cancelLabel = t('deny'), color: cancelColor } = cancel || {}

    return (
        <div className={classNames('n2o-popover', className)}>
            {isOpen && (
                <Popover {...rest} target={target} isOpen={isOpen}>
                    <>
                        <PopoverHeader>
                            <i className={classNames('fa fa-question-circle-o mr-1')} />
                            {title}
                        </PopoverHeader>
                        <PopoverBody>
                            <div className="mb-1">{text}</div>
                            <ButtonGroup className={classNames('d-flex justify-content-between', { 'flex-row-reverse': reverseButtons })}>
                                <Button color={cancelColor} className="btn-sm" onClick={onDeny}>
                                    {cancelLabel}
                                </Button>
                                <Button color={okColor} className="btn-sm" onClick={onConfirm}>
                                    {okLabel}
                                </Button>
                            </ButtonGroup>
                        </PopoverBody>
                    </>
                </Popover>
            )}
        </div>
    )
}

PopoverConfirm.propTypes = {
    className: PropTypes.string,
    title: PropTypes.oneOfType([PropTypes.string, Element]),
    children: PropTypes.oneOfType([PropTypes.string, Element]),
    ok: PropTypes.shape({
        label: PropTypes.string,
        color: PropTypes.string,
    }),
    cancel: PropTypes.shape({
        label: PropTypes.string,
        color: PropTypes.string,
    }),
    reverseButtons: PropTypes.bool,
    onDeny: PropTypes.func,
    onConfirm: PropTypes.func,
    isOpen: PropTypes.bool,
    text: PropTypes.string,
    target: PropTypes.string,
    onCancel: PropTypes.func,
}

PopoverConfirm.defaultProps = {
    className: '',
    title: 'Вы уверены?',
    text: '',
    onConfirm: () => {},
    onCancel: () => {},
}

export default PopoverConfirm
