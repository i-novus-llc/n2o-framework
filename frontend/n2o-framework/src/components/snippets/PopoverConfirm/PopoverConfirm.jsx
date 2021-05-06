import React from 'react'
import PropTypes from 'prop-types'
import { useTranslation } from 'react-i18next'
import Popover from 'reactstrap/lib/Popover'
import PopoverHeader from 'reactstrap/lib/PopoverHeader'
import PopoverBody from 'reactstrap/lib/PopoverBody'
import Button from 'reactstrap/lib/Button'
import ButtonGroup from 'reactstrap/lib/ButtonGroup'
import classNames from 'classnames'

/**
 * PopoverConfirm
 * @reactProps {string} className - className компонента
 * @reactProps {string} title - заголовок PopoverConfirm
 * @reactProps {string} okLabel - текст кнопки положительного ответа
 * @reactProps {string} cancelLabel - текст кнопки отрицательного ответа
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
        okLabel = t('confirm'),
        cancelLabel = t('deny'),
        target,
        onDeny,
        onConfirm,
        isOpen,
        ...rest
    } = props

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
                            <ButtonGroup className="d-flex justify-content-between">
                                <Button className="btn-sm" onClick={onDeny}>
                                    {cancelLabel}
                                </Button>
                                <Button className="btn-sm" onClick={onConfirm}>
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
    okLabel: PropTypes.string,
    cancelLabel: PropTypes.string,
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
