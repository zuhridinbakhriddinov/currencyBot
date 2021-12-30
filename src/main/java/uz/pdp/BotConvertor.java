package uz.pdp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BotConvertor extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

        SendMessage sendMessage = new SendMessage();
        String inputText = update.getMessage().getText();

        if (inputText.equals("/start")) {
            String str = update.getMessage().getFrom().getFirstName();
            sendMessage.setText(str + " Welcome to currency converter \uD83D\uDCB0");

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            sendMessage.setReplyMarkup(keyboardMarkup);

            keyboardMarkup.setResizeKeyboard(true);
            keyboardMarkup.setOneTimeKeyboard(true);
            keyboardMarkup.setSelective(true);

            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row1 = new KeyboardRow();
            row1.add(new KeyboardButton("Currency to sum"));
            row1.add(new KeyboardButton("Sum to Currency"));
            keyboard.add(row1);
            keyboardMarkup.setKeyboard(keyboard);
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (inputText.equals("Sum to Currency")) {
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText("Please enter at the end of the number 'S'\ne.g.100000S");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (inputText.equals("Currency to sum")) {
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText("Please enter at the end of the number 'C'\ne.g.100C");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            try {
                double curr = Double.parseDouble(inputText.substring(0, inputText.length() - 1));

                if (inputText.toLowerCase().endsWith("c")) {
                    cur(update, sendMessage, curr);
                } else if (inputText.toLowerCase().endsWith("s")) {
                    sumUz(update, sendMessage, curr);
                } else {
                    sendMessage.setText("Xato son kiritildi");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    execute(sendMessage);
                }
            } catch (Exception ignored) {
                sendMessage.setText("Xato son kiritildi");
                sendMessage.setChatId(update.getMessage().getChatId().toString());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void cur(Update update, SendMessage sendMessage, double cur) {

        try {
            Gson gson = new Gson();
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/ ");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Type type = new TypeToken<ArrayList<Currency>>() {
            }.getType();
            ArrayList<Currency> currencies = gson.fromJson(reader, type);

            for (Currency currency : currencies) {

                if (currency.getId() == 69) {
//
                    sendMessage.setText(String.valueOf("Today's rate: "+LocalDate.now())+"  \uD83D\uDCC6");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage.setText(cur + " USD - " + cur * Double.parseDouble(currency.getRate()) + " SUM");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (currency.getId() == 21) {
                    sendMessage.setText(cur + " EUR - " + cur * Double.parseDouble(currency.getRate()) + " SUM");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (currency.getId() == 15) {
                    sendMessage.setText(cur + " CNY - " + cur * Double.parseDouble(currency.getRate()) + " SUM");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sumUz(Update update, SendMessage sendMessage, double cur) {
        try {
            Gson gson = new Gson();
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/ ");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Type type = new TypeToken<ArrayList<Currency>>() {
            }.getType();
            ArrayList<Currency> currencies = gson.fromJson(reader, type);

            for (Currency currency : currencies) {

                if (currency.getId() == 69) {
                    sendMessage.setText(String.valueOf("Today's rate: "+LocalDate.now())+"  \uD83D\uDCC6");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage.setText(Math.round(cur / Double.parseDouble(currency.getRate()) * 100) / 100.0 + " USD");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (currency.getId() == 21) {
                    sendMessage.setText(Math.round(cur / Double.parseDouble(currency.getRate()) * 100) / 100.0 + " EUR");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (currency.getId() == 15) {
                    sendMessage.setText(Math.round(cur / Double.parseDouble(currency.getRate()) * 100) / 100.0 + " CNY");
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "currency_b7_bot";
    }

    @Override
    public String getBotToken() {
        return "5080862320:AAGDXiSxjb_eiotA_1oh37-kl2lJsdxnDAU";
    }
}
