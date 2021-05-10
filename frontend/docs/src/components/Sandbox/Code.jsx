import React, { memo } from 'react'
import PropTypes from 'prop-types'
import classnames from 'classnames'
import Highlight, { defaultProps } from 'prism-react-renderer'
import oceanicNext from 'prism-react-renderer/themes/oceanicNext'

import { getFileLang } from './utils'
import styles from './sandbox.module.scss'

const CodeBody = ({ fileName, source }) => (
    <Highlight {...defaultProps} code={source} theme={oceanicNext} language={getFileLang(fileName)}>
        {({ className, style, tokens, getLineProps, getTokenProps }) => (
            <pre className={classnames(className, styles.code)} style={style}>
            {tokens.map((line, i) => {
                const { className, ...lineProps } = getLineProps({ line, key: i })

                return (
                    <div
                        key={i}
                        className={classnames(className, styles.codeLine)}
                        {...lineProps}
                    >
                        <span className={styles.codeLineNumber}>{i + 1}</span>
                        <span className={styles.codeLineContent}>
                            {line.map((token, key) => (
                                <span {...getTokenProps({ token, key })} />
                            ))}
                        </span>
                    </div>
                )
            })}
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
