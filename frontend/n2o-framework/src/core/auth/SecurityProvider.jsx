import { Children } from 'react'
import PropTypes from 'prop-types'
import { withContext } from 'recompose'

const SecurityProvider = ({ children }) => Children.only(children)

SecurityProvider.propTypes = {
    authProvider: PropTypes.func,
    redirectPath: PropTypes.string,
    externalLoginUrl: PropTypes.string,
}

export default withContext(
    {
        authProvider: PropTypes.func,
        redirectPath: PropTypes.string,
        externalLoginUrl: PropTypes.string,
    },
    ({ authProvider, redirectPath, externalLoginUrl }) => ({
        authProvider,
        redirectPath,
        externalLoginUrl,
    }),
)(SecurityProvider)
