import React from 'react'

/**
 * Inline-спиннер (лоадер), находится справа
 */
export const InlineSpinner = () => (
    <div className="text-center">
        <div className="spinner-border text-muted" role="status">
            <span className="sr-only">Loading...</span>
        </div>
    </div>
)
