import React from 'react';

class ImageControl extends React.Component {
    render() {
        console.log(this.props)
        return (
            <div>
                <h1>Пример контрола Image</h1>
                <p>Кликните по картинке, чтобы вызвать экшен</p>
                <img width={400} src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/React-icon.svg/1200px-React-icon.svg.png" alt=""/>
            </div>
        );
    }
}

export default ImageControl;