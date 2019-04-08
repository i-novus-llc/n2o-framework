import React from 'react';
import PropTypes from 'prop-types';
import Icon from 'n2o/lib/components/snippets/Icon/Icon';

/**
 * Кастомный компонент IconHeader
 * @reactProps {string} icon - иконка
 * @reactProps {string} label - текст
 */
class IconHeader extends React.Component {
    render() {
        return (
            <div>
                {this.props.icon && <Icon name={this.props.icon} />}
                {this.props.label}
            </div>
        );
    }
}

IconHeader.propTypes = {
  icon: PropTypes.string,
  label: PropTypes.string
};

export default IconHeader;