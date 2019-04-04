import React from 'react';
import PropTypes from 'prop-types';
import StandartField from 'n2o/lib/components/widgets/Form/fieldsets/BlankFieldset';

class BackgroundImageFieldset extends React.Component {
    render() {
        const {height, title, backgroundImageSize} = this.props;
        return (
            <div style={{
                padding: '40px'
            }}>
                <h1>{title}</h1>
                <div style={{
                    background: 'url("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/React-icon.svg/1200px-React-icon.svg.png") no-repeat',
                    backgroundSize: backgroundImageSize,
                    height: height ? `${height}` : '200px'
                }}>
                    <StandartField
                        children={this.props.children}
                    />
                </div>
            </div>
        );
    }
}

BackgroundImageFieldset.propTypes = {
  backgroundImageSize: PropTypes.string,
  height: PropTypes.string,
  title: PropTypes.string
};

export default BackgroundImageFieldset;