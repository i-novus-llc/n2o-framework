import React from 'react'

export default function SimpleTemplate({ children }) {
    return (
        <div className="application">
            <div className="application-body container-fluid">{children}</div>
        </div>
    )
}
