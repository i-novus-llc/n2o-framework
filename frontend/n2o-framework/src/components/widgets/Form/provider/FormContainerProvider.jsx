import React, { createContext, useContext } from 'react'
import PropTypes from 'prop-types'

const defaultValue = {
    onBlur: () => {},
}

export const formContainerContext = createContext(defaultValue)

export const FormContainerProvider = ({ children, onBlur }) => (
    <formContainerContext.Provider value={{ onBlur }}>
        {children}
    </formContainerContext.Provider>
)

export const useFormContainerContext = () => {
    const context = useContext(formContainerContext)

    return context || defaultValue
}

FormContainerProvider.propTypes = {
    onBlur: PropTypes.string,
    children: PropTypes.any,
}

FormContainerProvider.displayName = 'FormContainerProvider'
