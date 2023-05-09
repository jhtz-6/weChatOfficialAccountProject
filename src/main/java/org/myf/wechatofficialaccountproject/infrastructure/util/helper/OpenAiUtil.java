package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.completions.Completion;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.ChoiceDTO;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.OpenAiResponse;

/**
 * @Author: myf
 * @CreateTime: 2023-05-08 15:50
 * @Description: OpenAiUtil
 */
public final class OpenAiUtil {

    public static String BuildResultTextByGPT3_5(OpenAiResponse openAiResponse) {
        StringBuilder ResultText = new StringBuilder();
        if (!StringUtils.equalsAny(openAiResponse.getModel(), ChatCompletion.Model.GPT_3_5_TURBO.getName(),
            ChatCompletion.Model.GPT_3_5_TURBO_0301.getName())) {
            return ResultText.toString();
        }
        for (ChoiceDTO choiceDTO : openAiResponse.getChoices()) {
            if (StringUtils.isNotBlank(choiceDTO.getDelta().getContent())) {
                ResultText.append(choiceDTO.getDelta().getContent());
            }
        }
        return ResultText.toString();
    }

    public static String BuildResultTextByGPT3(OpenAiResponse openAiResponse) {
        StringBuilder ResultText = new StringBuilder();
        if (!StringUtils.equalsAny(openAiResponse.getModel(), Completion.Model.DAVINCI_003.getName())) {
            return ResultText.toString();
        }
        for (ChoiceDTO choiceDTO : openAiResponse.getChoices()) {
            if (StringUtils.isNotBlank(choiceDTO.getText())) {
                ResultText.append(CommonUtil.unicodeToUtf8(choiceDTO.getText()));
            }
        }
        return ResultText.toString();
    }
}
