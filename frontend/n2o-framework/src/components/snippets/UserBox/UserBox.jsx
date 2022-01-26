import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { compose, withHandlers, withState, lifecycle } from 'recompose'
import { UncontrolledTooltip } from 'reactstrap'

import { id } from '../../../utils/id'

/**
 *
 * @param id - userbox id
 * @param image - изображение userbox
 * @param title - заголовок
 * @param subTitle - подзаголовок
 * @param children - subItems
 * @param isOpen - флаг видимости subItems
 * @param onToggle - переключение subItems
 * @param compressed - флаг сжатого вида
 * @returns {*}
 * @constructor
 */
function UserBox({
    id,
    image,
    title,
    subTitle,
    children,
    isOpen,
    onToggle,
    compressed,
}) {
    return (
        <div
            className={classNames('n2o-user-box', {
                'n2o-user-box--compressed': compressed,
                'pb-0': isOpen,
            })}
        >
            {compressed && (title || subTitle) && (
                <UncontrolledTooltip placement="right" target={id}>
                    <div>{title}</div>
                    <small>{subTitle}</small>
                </UncontrolledTooltip>
            )}
            <div
                id={id}
                className="n2o-user-box__image d-flex justify-content-center"
                onClick={compressed && onToggle}
            >
                <img
                    className="d-block"
                    src={image}
                    alt={title}
                    width="70"
                    height="70"
                />
            </div>
            {!compressed && (
                <>
                    <button
                        type="button"
                        onClick={onToggle}
                        className="n2o-user-box__title pl-2 pr-2 text-center"
                    >
                        <span
                            className={classNames({
                                'n2o-user-box__title--chevron': children,
                                'n2o-user-box__title--chevron-up': isOpen,
                            })}
                        >
                            {title}
                        </span>
                    </button>
                    {subTitle && (
                        <small className="n2o-user-box__sub-title d-block pl-2 pr-2 text-center">
                            {subTitle}
                        </small>
                    )}
                </>
            )}
            {isOpen && children && (
                <div className="n2o-user-box__items">
                    <ul>{children}</ul>
                </div>
            )}
        </div>
    )
}

UserBox.propTypes = {
    id: PropTypes.string,
    image: PropTypes.string,
    title: PropTypes.string,
    subTitle: PropTypes.string,
    children: PropTypes.node,
    isOpen: PropTypes.bool,
    onToggle: PropTypes.func,
    compressed: PropTypes.bool,
}

export default compose(
    withState('isOpen', 'toggle', false),
    withHandlers({
        onToggle: ({ isOpen, toggle }) => () => toggle(!isOpen),
    }),
    lifecycle({
        componentDidMount() {
            this.setState({ id: id() })
        },
    }),
)(UserBox)
