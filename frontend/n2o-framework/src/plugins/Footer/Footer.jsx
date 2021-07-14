import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

function Footer({ textRight, textLeft, className, style }) {
    const { N2O_ELEMENT_VISIBILITY } = window

    if (N2O_ELEMENT_VISIBILITY && N2O_ELEMENT_VISIBILITY.footer === false) {
        style = { ...style, display: 'none' }
    }

    return (
        <footer className={classNames('n2o-footer bg-dark py-2', className)} style={style}>
            <div className="container-fluid text-white d-flex">
                {
                    textLeft && (
                        <div className="align-self-start w-100 ml-1 text-left">
                            {textLeft}
                        </div>
                    )
                }
                {
                    textRight && (
                        <div className="align-self-end w-100 mr-1 text-right">
                            {textRight}
                        </div>
                    )
                }
            </div>
        </footer>
    )
}

Footer.propTypes = {
    textRight: PropTypes.string,
    textLeft: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
}

export default Footer
