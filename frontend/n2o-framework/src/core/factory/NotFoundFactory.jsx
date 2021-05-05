import React from 'react'
import PropTypes from 'prop-types'
import Alert from 'reactstrap/lib/Alert'

function NotFoundFactory({ src, level }) {
    return (
        <Alert color="danger">

      Фабрике не удалось найти компонент:
            {src}
            {' '}
в
            {level}
        </Alert>
    )
}

NotFoundFactory.propTypes = {
    src: PropTypes.string.isRequired,
    level: PropTypes.string,
}

export default NotFoundFactory
