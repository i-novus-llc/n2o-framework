import React from 'react';
import PropTypes from 'prop-types';
import StandartField from 'n2o/lib/components/widgets/Form/fields/StandardField/StandardField';

/**
 * Кастомный компонент BackgroundImageField
 * @reactProps {string} image - url изображения
 * @reactProps {number} backgroundImageSize - размер фоновой картинки
 * @reactProps {string} repeat - свойство repeat
 */
class BackgroundImageField extends React.Component {
    render() {
        const { image, backgroundImageSize, repeat } = this.props;
        return (
            <div style={{
                background: image ? `url(${image}) ${repeat}` : '',
                backgroundSize: backgroundImageSize ? `${backgroundImageSize}` : '30px'
            }}>
                <StandartField
                    {...this.props}
                />
            </div>
        );
    }
}

BackgroundImageField.propTypes = {
  image: PropTypes.string,
  backgroundImageSize: PropTypes.string,
  repeat: PropTypes.string
};

export default BackgroundImageField;