import { FormControl, MenuItem, Select } from "@mui/material";
import { useTranslation } from "react-i18next";

const LanguageSelector = () => {

    const { i18n } = useTranslation();

    const handleChange = async (event) => {

        const language = event.target.value;

        localStorage.setItem("language", language);

        await i18n.changeLanguage(language);
    };

    return (

        <FormControl size="small">
            <Select
                value={i18n.language}
                onChange={handleChange}
                sx={{ minWidth: 130 }}>

                    <MenuItem value="en">English</MenuItem>
                    <MenuItem value="hi">हिंदी</MenuItem>
                    <MenuItem value="mr">मराठी</MenuItem>

            </Select>
        </FormControl>
    );
};

export default LanguageSelector;