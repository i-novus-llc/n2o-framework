/**
 * Created by emamoshin on 13.10.2017.
 */
import React from 'react'

/**
 * @ignore
 */
class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props)
        this.state = { error: null, errorInfo: null }
    }

    componentDidCatch(error, errorInfo) {
    // Catch errors in any components below and re-render with error message
        this.setState({
            error,
            errorInfo,
        })
    // You can also log error messages to an error reporting service here
    }

    render() {
        const { errorInfo, error } = this.state

        if (errorInfo) {
            // Error path
            return (
                <div className="container" style={{ paddingTop: 130 }}>
                    <div className="media">
                        {/* eslint-disable-next-line jsx-a11y/alt-text */}
                        <img className="d-flex py-4 px-5" src="./error.png" />
                        <div className="media-body">
                            {/* eslint-disable-next-line react/jsx-one-expression-per-line */}
                            <h5 className="mt-0 display-4 pt-4">Упс, что-то пошло не так...</h5>

              Попробуйте перезагрузить страницу или обратитесь к администратору.
                            <details tabIndex="-1" style={{ whiteSpace: 'pre-wrap' }}>
                                <code>{error && error.toString()}</code>
                                <code>{errorInfo.componentStack}</code>
                            </details>
                        </div>
                    </div>
                </div>
            )
        }

        // Normally, just render children
        // eslint-disable-next-line react/prop-types,react/destructuring-assignment
        return this.props.children
    }
}

ErrorBoundary.propTypes = {}

export default ErrorBoundary
