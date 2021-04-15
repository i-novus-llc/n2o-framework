import React, { memo } from 'react'
import PropTypes from 'prop-types'
import classnames from 'classnames'
import Highlight, { defaultProps } from 'prism-react-renderer'
import theme from 'prism-react-renderer/themes/nightOwl'

import { getFileLang } from './utils'
import styles from './sandbox.module.scss'

const CodeBody = ({ fileName, source }) => (
    <Highlight {...defaultProps} theme={theme} code={source} language={getFileLang(fileName)}>
        {({ className, style, tokens, getLineProps, getTokenProps }) => (
            <pre className={classnames(className, styles.code)} style={style}>
                {tokens.map((line, i) => (
                    <div {...getLineProps({ line, key: i })}>
                        {line.map((token, key) => (
                            <span {...getTokenProps({ token, key })} />
                        ))}
                    </div>
                ))}
            </pre>
        )}
    </Highlight>
)

CodeBody.propTypes = {
    fileName: PropTypes.string.isRequired,
    source: PropTypes.string.isRequired,
}

const Code = memo(CodeBody)

Code.propTypes = {
    fileName: PropTypes.string.isRequired,
    source: PropTypes.string.isRequired,
}

export { Code }
